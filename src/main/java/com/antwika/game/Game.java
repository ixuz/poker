package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import com.antwika.eval.core.IEvaluation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    @Getter
    private final String tableName;

    @Getter
    private long handId = 0L;

    @Getter
    private final List<Seat> seats = new ArrayList<>();

    @Getter
    private int buttonAt = 0;

    private int actionAt = 0;

    private int totalBet = 0;

    @Getter
    private int lastRaise = 0;

    @Getter
    private Long cards = 0L;

    private final List<Pot> pots = new ArrayList<>();

    private final Deck deck;

    private final Random prng;

    @Getter
    private final int smallBlind;

    @Getter
    private final int bigBlind;

    @Getter
    private int delivered = 0;

    private int chipsInPlay = 0;

    @Getter
    private final String gameType = "Hold'em No Limit";

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

    }

    public Seat firstAvailableSeat() {
        return seats.stream().filter(seat -> seat.getPlayer() == null).findFirst().orElse(null);
    }

    public void join(Player player) {
        final Seat available = firstAvailableSeat();
        if (available == null) return;

        available.setPlayer(player);
        available.setCards(0L);
        available.setStack(1000);
        available.setCommitted(0);
        available.setPostedSmallBlindLastRound(false);
        available.setPostedBigBlindLastRound(false);

        logger.info("{}: joined the game at seat #{}", player.getPlayerName(), available.getSeatIndex() + 1);
    }

    public void leave(Player player) {
        final Seat seat = getSeat(player);
        if (seat == null) return;

        seat.setPlayer(null);
        seat.setStack(0);

        logger.info("{}: left the game", player.getPlayerName());
    }

    public void drawButtonPosition() {
        logger.debug("Drawing cards to determine button position...");
        deck.resetAndShuffle();

        seats.stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> seat.setCards(deck.draw()));

        final List<Seat> sortedByCard = seats.stream()
                .filter(i -> i.getPlayer() != null)
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_SUIT_INDEX.get(e.getCards())))
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_RANK_INDEX.get(e.getCards())))
                .toList();

        final Seat winner = sortedByCard.get(sortedByCard.size() - 1);

        if (winner == null) {
            throw new RuntimeException("Failed to draw card for the button position!");
        }

        moveButtonTo(winner.getSeatIndex());

        seats.forEach(seat -> seat.setCards(0L));
    }

    public void moveButtonTo(int seatIndex) {
        buttonAt = seatIndex;
        logger.debug("Button is at seat #{}", buttonAt);
    }

    public boolean canDealNextHand() {
        final List<Seat> activeSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getPlayer() == null) continue;
            if (seat.getStack() == 0) continue;
            activeSeats.add(seat);
        }

        return activeSeats.size() > 1;
    }

    public void newHand() {
        totalBet = 0;
        lastRaise = 0;
        cards = 0L;
        delivered = 0;
        pots.clear();

        handId += 1L;

        final List<Seat> bustedSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStack() == 0) {
                bustedSeats.add(seat);
            }
        }

        for (Seat seat : bustedSeats) {
            final Player player = seat.getPlayer();
            if (player == null) continue;
            seat.setPlayer(null);
            logger.info("{} was forced to leave the game (busted)", player.getPlayerName());
        }

        chipsInPlay = 0;
        for (Seat seat : seats) {
            seat.setCards(0L);
            seat.setHasActed(false);
            seat.setHasFolded(false);
            chipsInPlay += seat.getStack();
        }

        if (handId == 1) {
            drawButtonPosition();
        }
        if (handId > 1) {
            final Seat nextButtonSeat = nextSeat(buttonAt, 0, true);
            buttonAt = nextButtonSeat.getSeatIndex();
        }
    }

    public void blinds() {
        forcePostBlind(buttonAt, 0, smallBlind);
        forcePostBlind(buttonAt, 1, bigBlind);
        totalBet = bigBlind;
        lastRaise = bigBlind;
    }

    public void forcePostBlind(int buttonAt, int blindIndex, int blindAmount) {
        final Seat seat = nextSeat(buttonAt, blindIndex, true);
        final Player player = seat.getPlayer();

        int commitAmount = Math.min(seat.getStack(), blindAmount);
        commit(seat, commitAmount);

        actionAt = nextSeat(seat.getSeatIndex(), 0, true).getSeatIndex();

        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: posts blind %d", player.getPlayerName(), commitAmount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
    }

    private int getPlayerRemainingCount() {
        final List<Seat> playerSeats = seats.stream()
                .filter(i -> i.getPlayer() != null)
                .toList();
        final List<Seat> foldedSeats = seats.stream()
                .filter(i -> i.getPlayer() != null)
                .filter(Seat::isHasFolded)
                .toList();
        return playerSeats.size() - foldedSeats.size();
    }

    public int getNumberOfPlayersThatCanAct() {
        final List<Seat> playerSeats = seats.stream()
                .filter(i -> i.getPlayer() != null)
                .filter(i -> i.getStack() > 0)
                .filter(i -> i.getCards() > 0L)
                .filter(i -> !i.isHasFolded())
                .toList();
        return playerSeats.size();
    }

    public void bettingRound() {
        if (Long.bitCount(cards) != 0) {
            for (Seat seat : seats) {
                seat.setHasActed(false);
            }
            actionAt = nextSeat(buttonAt, 0, true).getSeatIndex();
        }


        while (true) {
            if (getPlayerRemainingCount() == 1) {
                logger.debug("All but one player has folded, hand must end");
                break;
            }

            if (getNumberOfPlayersThatCanAct() < 2) {
                break;
            }

            final Seat seat = seats.get(actionAt);
            final Seat seatAfter = nextSeat(actionAt, 0, true);
            if (seat == null) break;

            if (seat.isHasFolded()) {
                seat.setHasActed(true);
                actionAt = seatAfter.getSeatIndex();
                continue;
            }

            final Player player = seat.getPlayer();

            final int toCall = Math.min(seat.getStack(), totalBet - seat.getCommitted());
            final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, bigBlind));
            final int minBet = totalBet + minRaise - seat.getCommitted();
            final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

            if (toCall > 0) {
                logger.debug("{}, {} to call", seat.getPlayer(), toCall);
            } else {
                logger.debug("{}, Check or bet?", seat.getPlayer());
            }

            if (seat.getStack() == 0) {
                break;
            }

            final Event response = player.handle(new PlayerActionRequest(player, this, totalBet, toCall, minBet, smallestValidRaise));

            handleEvent(response);

            if (hasAllPlayersActed()) {
                break;
            }

            final Seat theNextSeat = nextSeat(actionAt, 0, true);
            if (theNextSeat == null) {
                break;
            }

            actionAt = theNextSeat.getSeatIndex();
        }

        logger.debug("Betting round ended");
    }

    public void handleEvent(Event event) {
        if (event instanceof PlayerActionResponse e) {
            handlePlayerActionResponse(e);
        }
    }

    public void handlePlayerActionResponse(PlayerActionResponse e) {
        final Game game = e.game;
        final Seat seat = game.getSeat(e.player);
        final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

        switch (e.action) {
            case "FOLD" -> {
                seat.setHasFolded(true);
                logger.info("{}: folds", seat.getPlayer().getPlayerName());
            }
            case "CHECK" -> logger.info("{}: checks", seat.getPlayer().getPlayerName());
            case "BET" -> {
                if (e.amount > seat.getStack()) {
                    throw new RuntimeException("Player can not bet a greater amount than his remaining stack!");
                }
                if (e.amount == 0) {
                    throw new RuntimeException("Player can not bet a zero amount!");
                }

                commit(seat, e.amount);
                totalBet = seat.getCommitted();
                lastRaise = e.amount;
                final StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerName(), e.amount));
                if (seat.getStack() == 0) {
                    sb.append(" and is all-in");
                }
                logger.info(sb.toString());
            }
            case "RAISE" -> {
                if (e.amount > seat.getStack()) {
                    throw new RuntimeException("Player can not raise a greater amount than his remaining stack!");
                }

                if (e.amount < smallestValidRaise) {
                    throw new RuntimeException("Player must at least raise by one full big blind, or raise all-in for less");
                }
                commit(seat, e.amount);
                if (seat.getCommitted() > totalBet) {
                    totalBet = seat.getCommitted();
                    lastRaise = e.amount;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s: raises to %d", seat.getPlayer().getPlayerName(), seat.getCommitted()));
                if (seat.getStack() == 0) {
                    sb.append(" and is all-in");
                }
                logger.info(sb.toString());
            }
            case "CALL" -> {
                if (e.amount > seat.getStack()) {
                    throw new RuntimeException("Player can not call a greater amount than his remaining stack!");
                }

                commit(seat, e.amount);
                final StringBuilder sb = new StringBuilder();
                sb.append(String.format("%s: calls %d", seat.getPlayer().getPlayerName(), e.amount));
                if (seat.getStack() == 0) {
                    sb.append(" and is all-in");
                }
                logger.info(sb.toString());
            }
            default -> throw new RuntimeException("Player action response unknown");
        }

        seat.setHasActed(true);
    }

    private void commit(Seat seat, int amount) {
        if (seat.getStack() < amount) throw new RuntimeException("Commit amount is greater than the available stack");
        if (amount <= 0) throw new RuntimeException("Commit must be greater than zero");
        seat.setStack(seat.getStack() - amount);
        seat.setCommitted(seat.getCommitted() + amount);
    }

    public boolean hasAllPlayersActed() {
        int highestCommit = 0;
        for (Seat seat : seats) {
            if (highestCommit < seat.getCommitted()) {
                highestCommit = seat.getCommitted();
            }
        }

        for (Seat seat : seats) {
            if (seat.getStack() == 0) {
                continue;
            }

            if (seat.isHasFolded()) {
                continue;
            }

            if (!seat.isHasActed()) {
                return false;
            }

            if (seat.getCommitted() != highestCommit) {
                return false;
            }
        }

        return true;
    }

    public void collect() {
        pots.addAll(Pots.collectBets(seats));

        lastRaise = 0;
        totalBet = 0;

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(Seat::getStack).sum();
        int totalPot = getTotalPot(false);
        if (totalStacks + totalPot != chipsInPlay) {
            throw new RuntimeException("Invalid amount of chips");
        }
    }

    public Seat nextSeat(int fromSeat, int skips, boolean mustAct) {
        Seat nextSeat = null;
        int skipped = 0;

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed() && !seat.isHasFolded()) {
                    nextSeat = seat;
                } else if (seat.getCommitted() < totalBet && seat.getStack() > 0 && !seat.isHasFolded()) {
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

        GameLog.printTableSeatCardsInfo(this);
    }

    public String getStage() {
        return switch (Long.bitCount(cards)) {
            case 3 -> "FLOP";
            case 4 -> "TURN";
            case 5 -> "RIVER";
            default -> "PREFLOP";
        };
    }

    public void dealCommunityCards(int count) {
        long prev = getCards();
        long add = 0L;
        for (int i = 0; i < count; i += 1) {
            add |= deck.draw();
        }
        cards |= add;

        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("*** %s ***", getStage()));
            if (prev != 0L) {
                sb.append(String.format(" [%s]", GameUtil.toNotation(prev)));
            }
            if (add != 0L) {
                sb.append(String.format(" [%s]", GameUtil.toNotation(add)));
            }
            logger.info(sb.toString());
            logger.info("Total pot: {}", getTotalPot(false));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealFlop() {
        long flop = deck.draw() | deck.draw() | deck.draw();
        cards |= flop;
        try {
            logger.info("*** FLOP *** [{}]", GameUtil.toNotation(flop));
            logger.info("Total pot: {}", getTotalPot(false));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealTurn() {
        long flop = cards;
        long turn = deck.draw();
        cards |= turn;

        try {
            logger.info("*** TURN *** [{}] [{}]", GameUtil.toNotation(flop), GameUtil.toNotation(turn));
            logger.info("Total pot: {}", getTotalPot(false));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealRiver() {
        long flopAndTurn = cards;
        long river = deck.draw();
        cards |= river;

        try {
            logger.info("*** RIVER *** [{}] [{}]", GameUtil.toNotation(flopAndTurn), GameUtil.toNotation(river));
            logger.info("Total pot: {}", getTotalPot(false));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTotalPot(boolean includeCommitted) {
        int sum = pots.stream().mapToInt(Pot::getTotalAmount).sum();

        if (includeCommitted) {
            sum += seats.stream().mapToInt(Seat::getCommitted).sum();
        }

        return sum;
    }

    public Seat getSeat(Player player) {
        return seats.stream()
                .filter(i -> i.getPlayer() == player)
                .findFirst()
                .orElse(null);
    }

    public void showdown() throws NotationException {
        logger.debug("Showdown");
        final List<Pot> collapsed = Pots.collapsePots(pots);
        final List<Candidate> winners = Pots.determineWinners(collapsed, cards, buttonAt, seats.size());

        for (Candidate winner : winners) {
            final Seat seat = winner.getSeat();
            final Player player = seat.getPlayer();
            int amount = winner.getAmount();
            delivered += amount;
            winner.getSeat().setStack(seat.getStack() + amount);

            logger.info("{} collected {} from {}",
                    player.getPlayerName(),
                    amount,
                    winner.getPotName());
        }
        pots.clear();

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(Seat::getStack).sum();
        int totalPot = getTotalPot(false);
        if (totalStacks + totalPot != chipsInPlay) {
            throw new RuntimeException("Invalid amount of chips");
        }

        GameLog.printSummary(this);
    }

    public void dealHand() throws NotationException {
        logger.info("--- HAND BEGIN ---");
        newHand();
        GameLog.printGameInfo(this);
        deck.resetAndShuffle();
        GameLog.printTableInfo(this);
        GameLog.printTableSeatsInfo(this);
        blinds();
        dealCards();
        bettingRound();
        collect();
        if (getPlayerRemainingCount() > 1) {
            dealFlop();
            bettingRound();
            collect();
        }
        if (getPlayerRemainingCount() > 1) {
            dealTurn();
            bettingRound();
            collect();
        }
        if (getPlayerRemainingCount() > 1) {
            dealRiver();
            bettingRound();
            collect();
        }
        showdown();
        logger.info("--- HAND END ---");
    }
}
