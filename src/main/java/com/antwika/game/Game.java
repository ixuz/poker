package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import com.antwika.eval.core.IEvaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game extends EventHandler {
    private static Logger logger = LoggerFactory.getLogger(Game.class);

    private String tableName;

    private long id = 0L;

    private List<Seat> seats = new ArrayList<>();

    private int buttonAt = 0;

    private int actionAt = 0;

    private int totalBet = 0;

    private int lastRaise = 0;

    private Long cards = 0L;

    private final List<Pot> pots = new ArrayList<>();

    private final Deck deck;

    private final Random prng;

    private final int smallBlind;

    private final int bigBlind;

    private int delivered = 0;

    public Game(long prngSeed, String tableName, int seatCount, int smallBlind, int bigBlind) {
        this.prng = new Random(prngSeed);
        this.tableName = tableName;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;

        for (int i = 0; i < seatCount; i += 1) {
            final Seat seat = new Seat();
            seat.setSeatIndex(i);
            seats.add(seat);
        }

        deck = new Deck(prng.nextInt());

        logger.info("Game with {} seats opened", seats.size());
    }

    public void join(Player player) {
        Seat available = null;
        for (Seat seat : seats) {
            if (seat.getPlayer() == null) {
                available = seat;
                break;
            }
        }

        if (available == null) return;

        available.setPlayer(player);
        available.setCards(0L);
        available.setStack(1000);
        available.setCommitted(0);
        available.setPostedSmallBlindLastRound(false);
        available.setPostedBigBlindLastRound(false);

        logger.info("{} joined the game at seat #{}", player, available.getSeatIndex());
    }

    public void leave(Player player) {
        for (Seat seat : seats) {
            if (seat.getPlayer() == player) {
                seat.setPlayer(null);
                seat.setStack(0);
            }
        }
        logger.info("{} left the game", player);
    }

    public void drawButtonPosition() throws NotationException {
        logger.debug("Drawing cards to determine button position...");
        deck.resetAndShuffle();

        for (final Seat seat : seats) {
            if (seat.getPlayer() == null) continue;

            long card = deck.draw();
            seat.setCards(card);
            logger.debug("Seat #{} got card: {}", seat.getSeatIndex(), HandUtil.toNotation(card));
        }

        final List<Seat> seatCardByRankAndSuit = seats.stream()
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_SUIT_INDEX.get(e.getCards())))
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_RANK_INDEX.get(e.getCards())))
                .toList();

        Seat seatWithHighestCardAndSuit = null;
        for (int i = seats.size() - 1; i >= 0; i -= 1) {
            final Seat seat = seats.get(i);

            if (seat.getPlayer() == null) continue;

            seatWithHighestCardAndSuit = seat;
            break;
        }

        if (seatWithHighestCardAndSuit == null) {
            throw new RuntimeException("Failed to draw card for the button position!");
        }

        moveButtonTo(seatWithHighestCardAndSuit.getSeatIndex());

        for (final Seat seat : seats) {
            seat.setCards(0L);
        }
    }

    public void moveButtonTo(int seatIndex) {
        buttonAt = seatIndex;
        logger.debug("Button is at seat #{}", buttonAt);
    }

    public void incrementHandId() {
        id += 1L;
    }

    public void blinds() {
        int smallBlindAmount = 5;
        seats.get(1).setStack(seats.get(1).getStack() - smallBlindAmount);
        seats.get(1).setCommitted(seats.get(1).getCommitted() + smallBlindAmount);
        actionAt += 1;
        logger.info("{}: posts small blind {}",
                seats.get(1).getPlayer().getPlayerName(),
                smallBlindAmount);

        int bigBlindAmount = 10;
        seats.get(2).setStack(seats.get(2).getStack() - bigBlindAmount);
        seats.get(2).setCommitted(seats.get(2).getCommitted() + bigBlindAmount);
        actionAt += 1;
        logger.info("{}: posts big blind {}",
                seats.get(2).getPlayer().getPlayerName(),
                bigBlindAmount);

        totalBet = bigBlindAmount;
        lastRaise = bigBlindAmount;
    }

    public void bettingRound() {
        for (Seat seat : seats) {
            seat.setHasActed(false);
        }

        while (true) {
            final Seat seat = nextSeat(actionAt, 0, true);
            final Seat seatAfter = nextSeat(actionAt, 1, true);
            if (seat == null) break;

            final Player player = seat.getPlayer();

            final int toCall = totalBet - seat.getCommitted();
            final int minCompleteRaise = Math.max(lastRaise, 10);
            final int minCompleteBet = totalBet + minCompleteRaise;
            final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, 10));
            final int minBet = totalBet + minRaise - seat.getCommitted();

            if (toCall > 0) {
                logger.debug("{}, {} to call", seat.getPlayer(), toCall);
            } else {
                logger.debug("{}, Check or bet?", seat.getPlayer());
            }

            final Event response = player.handle(new PlayerActionRequest(player, this, toCall, minBet, minRaise));

            if (response instanceof PlayerActionResponse e) {
                if (e.action.equals("CHECK")) {
                    seat.setHasActed(true);
                    logger.info("{}: checks", seat.getPlayer().getPlayerName());
                } else if (e.action.equals("BET")) {
                    seat.setHasActed(true);
                    seat.setCommitted(seat.getCommitted() + e.amount);
                    seat.setStack(seat.getStack() - e.amount);
                    totalBet = seat.getCommitted();
                    lastRaise = e.amount;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerName(), e.amount));
                    if (seat.getStack() == 0) {
                        sb.append("and is all-in");
                    }
                    logger.info(sb.toString());
                } else if (e.action.equals("RAISE")) {
                    seat.setHasActed(true);
                    seat.setCommitted(seat.getCommitted() + e.amount);
                    seat.setStack(seat.getStack() - e.amount);
                    if (seat.getCommitted() > totalBet) {
                        totalBet = seat.getCommitted();
                        lastRaise = e.amount;
                    }
                    final StringBuilder sb = new StringBuilder();
                    sb.append(String.format("%s: raise %d", seat.getPlayer().getPlayerName(), e.amount));
                    if (seat.getStack() == 0) {
                        sb.append("and is all-in");
                    }
                    logger.info(sb.toString());
                } else if (e.action.equals("CALL")) {
                    seat.setHasActed(true);
                    seat.setCommitted(seat.getCommitted() + e.amount);
                    seat.setStack(seat.getStack() - e.amount);
                    final StringBuilder sb = new StringBuilder();
                    sb.append(String.format("%s: calls %d", seat.getPlayer().getPlayerName(), e.amount));
                    if (seat.getStack() == 0) {
                        sb.append("and is all-in");
                    }
                    logger.info(sb.toString());
                }
            }

            actionAt = seatAfter.getSeatIndex();
        }

        logger.debug("Betting round ended");
    }

    public void collect() {
        pots.addAll(Pots.collectBets(seats));

        lastRaise = 0;
        totalBet = 0;

        int totalPot = pots.stream().mapToInt(Pot::getTotalAmount).sum();

        logger.info("Total pot {}", totalPot);
    }

    public Seat nextSeat(int fromSeat, int skips, boolean mustAct) {
        Seat nextSeat = null;
        int skipped = 0;

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed()) {
                    nextSeat = seat;
                } else if (seat.getCommitted() < totalBet && seat.getStack() > 0) {
                    nextSeat = seat;
                }
            } else {
                nextSeat = seat;
            }

            if (nextSeat != null) {
                if (skipped < skips) {
                    skipped += 1;
                    continue;
                }
                break;
            }
        }

        return nextSeat;
    }

    public void dealCards() throws NotationException {
        logger.info("*** HOLE CARDS ***");
        for (int i = 0; i < seats.size() * 2; i += 1) {
            final int seatIndex = (actionAt + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);
            final long card = deck.draw();
            seat.setCards(seat.getCards() | card);
            try {
                logger.debug("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        printTableSeatCardsInfo();
    }

    public void dealFlop() {
        long flop = deck.draw() | deck.draw() | deck.draw();
        cards |= flop;
        try {
            logger.info("*** FLOP *** [{}]", getCardsNotation(flop));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealTurn() {
        long flop = cards;
        long turn = deck.draw();
        cards |= turn;

        try {
            logger.info("*** TURN *** [{}] [{}]", getCardsNotation(flop), getCardsNotation(turn));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealRiver() {
        long flopAndTurn = cards;
        long river = deck.draw();
        cards |= river;

        try {
            logger.info("*** RIVER *** [{}] [{}]", getCardsNotation(flopAndTurn), getCardsNotation(river));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    @ToString(onlyExplicitlyIncluded = true)
    public class SeatHand {
        @ToString.Include
        public Seat seat;
        public long hand;
        @ToString.Include
        public IEvaluation evaluation;
        public String groupId;
    }

    public int getTotalPot(boolean includeCommitted) {
        int sum = 0;

        for (Pot pot : pots) {
            sum += pot.getTotalAmount();
        }

        for (Seat seat : seats) {
            sum += seat.getCommitted();
        }

        return sum;
    }

    public int getLastRaise() {
        return lastRaise;
    }

    public Seat getSeat(Player player) {
        for (Seat seat : seats) {
            if (seat.getPlayer() == player) {
                return seat;
            }
        }
        return null;
    }

    public void showdown() throws NotationException {
        logger.debug("Showdown");
        final List<Pot> collapsed = Pots.collapsePots(pots);
        final List<Candidate> winners = Pots.determineWinners(collapsed, cards, buttonAt);

        for (Candidate winner : winners) {
            final Seat seat = winner.getSeat();
            final Player player = seat.getPlayer();
            int seatIndex = seat.getSeatIndex();
            int amount = winner.getAmount();
            delivered += amount;
            winner.getSeat().setStack(seat.getStack() + amount);

            logger.info("{} collected {} from {}",
                    player.getPlayerName(),
                    amount,
                    winner.getPotName());
        }

        printSummary();

        /*final IHandProcessor handProcessor = new TexasHoldemProcessor();

        final List<SeatHand> seatHands = new ArrayList<>();

        for (Seat seat : seats) {
            long hand = cards | seat.getCards();
            seatHands.add(new SeatHand(seat, hand, HandEvaluatorUtil.evaluate(handProcessor, hand), null));
        }

        seatHands.sort((e1, e2) -> {
            try {
                return HandEvaluatorUtil.compare(handProcessor, e1.hand, e2.hand);
            } catch (HandEvaluatorException e) {
                throw new RuntimeException(e);
            }
        });

        Collections.reverse(seatHands);

        // TODO: Group winners
        List<String> orderedGroupIds = new ArrayList<>();

        for (SeatHand seatHand : seatHands) {
            final StringBuilder groupIdBuilder = new StringBuilder();
            IEvaluation evaluation = seatHand.evaluation;
            final long handType = evaluation.getHandType();
            groupIdBuilder.append(String.format("handType=%d", handType));
            final int[] kickers = evaluation.getKickers();
            if (kickers.length > 0) {
                groupIdBuilder.append("&kickers=");
                for (int i = 0; i < kickers.length; i += 1) {
                    int kicker = kickers[i];
                    groupIdBuilder.append(kicker);
                    if (i < kickers.length - 1) {
                        groupIdBuilder.append(",");
                    }
                }
            }

            final String groupId = groupIdBuilder.toString();
            seatHand.groupId = groupId;

            if (orderedGroupIds.isEmpty() || !orderedGroupIds.get(orderedGroupIds.size() - 1).equals(groupId)) {
                orderedGroupIds.add(groupId);
            }
        }

        final Map<String, List<SeatHand>> groupedWinners = seatHands.stream().collect(Collectors.groupingBy(item -> item.groupId));

        for (String groupId : orderedGroupIds) {
            logger.info("Winner groupId: {}", groupId);
            List<SeatHand> winners = groupedWinners.get(groupId);
            for (SeatHand winner : winners) {
                logger.info("  Winner : {}", winner.seat.getPlayer());
            }
        }*/
    }

    public void dealHand() throws NotationException {
        incrementHandId();
        printGameInfo();
        drawButtonPosition();
        deck.resetAndShuffle();
        printTableInfo();
        printTableSeatsInfo();
        blinds();
        dealCards();
        bettingRound();
        collect();
        dealFlop();
        bettingRound();
        collect();
        dealTurn();
        bettingRound();
        collect();
        dealRiver();
        bettingRound();
        collect();
        showdown();
    }

    public void printGameInfo() {
        logger.info("Poker Hand #{}: Hold'em No Limit ({}/{}) - {}",
                id,
                smallBlind,
                bigBlind,
                new Date());
    }

    public void printTableInfo() {
        logger.info("Table '{}' {}-max Seat #{} is the button",
                tableName,
                seats.size(),
                buttonAt);
    }

    public void printTableSeatsInfo() {
        for (Seat seat : seats) {
            if (seat.getPlayer() == null) continue;

            logger.info("Seat {}: {} ({} in chips) ",
                    seat.getSeatIndex() + 1,
                    seat.getPlayer().getPlayerName(),
                    seat.getStack());
        }
    }

    public void printTableSeatCardsInfo() throws NotationException {
        for (Seat seat : seats) {
            if (seat.getPlayer() == null) continue;

            final long cards = seat.getCards();

            if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

            final String handNotation = getCardsNotation(seat.getCards());

            logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerName(), handNotation);
        }
    }

    public void printSummary() throws NotationException {
        logger.info("*** SUMMARY ***");
        logger.info("Total pot {} | Rake {}", delivered, 0);
        logger.info("Board [{}]", getCardsNotation(cards));

        for (Seat seat : seats) {
            if (seat.getPlayer() == null) continue;

            final Player player = seat.getPlayer();

            logger.info("Seat {}: {} stack {}",
                    seat.getSeatIndex(),
                    player.getPlayerName(),
                    seat.getStack());
        }
    }

    private String getCardsNotation(long cards) throws NotationException {
        final String cardsNotation = HandUtil.toNotation(cards);

        final List<String> cn = new ArrayList<>();
        for (int i = 0; i < cardsNotation.length(); i += 2) {
            final String cardNotation = cardsNotation.substring(i, i+2);
            cn.add(cardNotation);
        }

        return String.join(" ", cn);
    }
}
