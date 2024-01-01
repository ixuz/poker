package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import lombok.Getter;
import lombok.Setter;
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
    @Setter
    private int buttonAt = 0;

    @Getter
    @Setter
    private int actionAt = 0;

    @Getter
    private int totalBet = 0;

    @Getter
    private int lastRaise = 0;

    @Getter
    private Long cards = 0L;

    @Getter
    private final List<Pot> pots = new ArrayList<>();

    @Getter
    private final Deck deck;

    @Getter
    private Prng prng;

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
        this.prng = new Prng(prngSeed);
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

    public void startHand() {
        logger.info("--- HAND BEGIN ---");
        pots.clear();
        handId += 1L;
        cards = 0L;
        delivered = 0;
        totalBet = bigBlind;
        lastRaise = bigBlind;
        GameUtil.unseat(GameUtil.findAllBustedSeats(this));
        GameUtil.resetAllSeats(this);
        chipsInPlay = GameUtil.countAllStacks(this);
    }

    public void endHand() {
        GameUtil.pushButton(this);
        logger.info("--- HAND END ---");
    }

    public void bettingRound() {
        GameUtil.prepareBettingRound(this);

        while (true) {
            if (GameUtil.countPlayersRemainingInHand(this) == 1) {
                logger.debug("All but one player has folded, hand must end");
                break;
            }

            if (GameUtil.getNumberOfPlayersLeftToAct(this) < 2) {
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

        switch (e.action) {
            case FOLD -> handlePlayerFoldAction(e);
            case CHECK -> handlePlayerCheckAction(e);
            case CALL -> handlePlayerCallAction(e);
            case BET -> handlePlayerBetAction(e);
            case RAISE -> handlePlayerRaiseAction(e);
            default -> throw new RuntimeException("Player action response unknown");
        }

        seat.setHasActed(true);
    }

    public void handlePlayerFoldAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = getSeat(playerActionResponse.player);
        seat.setHasFolded(true);
        logger.info("{}: folds", seat.getPlayer().getPlayerName());
    }

    public void handlePlayerCheckAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = getSeat(playerActionResponse.player);
        seat.setHasActed(true);
        logger.info("{}: checks", seat.getPlayer().getPlayerName());
    }

    public void handlePlayerBetAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = getSeat(playerActionResponse.player);
        if (playerActionResponse.amount > seat.getStack()) {
            throw new RuntimeException("Player can not bet a greater amount than his remaining stack!");
        }
        if (playerActionResponse.amount == 0) {
            throw new RuntimeException("Player can not bet a zero amount!");
        }

        GameUtil.commit(seat, playerActionResponse.amount);
        totalBet = seat.getCommitted();
        lastRaise = playerActionResponse.amount;
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerName(), playerActionResponse.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
    }

    public void handlePlayerRaiseAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = getSeat(playerActionResponse.player);
        if (playerActionResponse.amount > seat.getStack()) {
            throw new RuntimeException("Player can not raise a greater amount than his remaining stack!");
        }

        final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());
        if (playerActionResponse.amount < smallestValidRaise) {
            throw new RuntimeException("Player must at least raise by one full big blind, or raise all-in for less");
        }

        GameUtil.commit(seat, playerActionResponse.amount);
        if (seat.getCommitted() > totalBet) {
            totalBet = seat.getCommitted();
            lastRaise = playerActionResponse.amount;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: raises to %d", seat.getPlayer().getPlayerName(), seat.getCommitted()));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
    }

    public void handlePlayerCallAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = getSeat(playerActionResponse.player);
        if (playerActionResponse.amount > seat.getStack()) {
            throw new RuntimeException("Player can not call a greater amount than his remaining stack!");
        }

        GameUtil.commit(seat, playerActionResponse.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: calls %d", seat.getPlayer().getPlayerName(), playerActionResponse.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
    }

    public boolean hasAllPlayersActed() {
        return GameUtil.hasAllPlayersActed(this);
    }

    public void collect() {
        pots.addAll(Pots.collectBets(seats));

        lastRaise = 0;
        totalBet = 0;

        int totalStacks = GameUtil.countAllStacks(this);
        int totalPot = GameUtil.countTotalPot(this);
        if (totalStacks + totalPot != chipsInPlay) {
            throw new RuntimeException("Invalid amount of chips");
        }
    }

    public Seat nextSeat(int fromSeat, int skips, boolean mustAct) {
        return GameUtil.findNextSeatToAct(this, fromSeat, skips, mustAct);
    }

    public void dealCards() throws NotationException {
        logger.info("*** HOLE CARDS ***");
        for (int i = 0; i < seats.size() * 2; i += 1) {
            final int seatIndex = (actionAt + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);
            if (seat.getPlayer() == null) continue;
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

    public void dealCommunityCards(int count) {
        long prev = getCards();
        long add = 0L;
        for (int i = 0; i < count; i += 1) {
            add |= deck.draw();
        }
        cards |= add;

        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("*** %s ***", GameUtil.getStreetName(this)));
            if (prev != 0L) {
                sb.append(String.format(" [%s]", GameUtil.toNotation(prev)));
            }
            if (add != 0L) {
                sb.append(String.format(" [%s]", GameUtil.toNotation(add)));
            }
            logger.info(sb.toString());
            logger.info("Total pot: {}", GameUtil.countTotalPot(this));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
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
        int totalPot = GameUtil.countTotalPot(this);
        if (totalStacks + totalPot != chipsInPlay) {
            throw new RuntimeException("Invalid amount of chips");
        }

        GameLog.printSummary(this);
    }

    public void dealHand() throws NotationException {
        startHand();
        GameLog.printGameInfo(this);
        deck.resetAndShuffle();
        GameLog.printTableInfo(this);
        GameLog.printTableSeatsInfo(this);
        GameUtil.forcePostBlinds(this, List.of(smallBlind, bigBlind));
        dealCards();
        bettingRound();
        collect();
        if (GameUtil.countPlayersRemainingInHand(this) > 1) {
            dealCommunityCards(3);
            bettingRound();
            collect();
        }
        if (GameUtil.countPlayersRemainingInHand(this) > 1) {
            dealCommunityCards(1);
            bettingRound();
            collect();
        }
        if (GameUtil.countPlayersRemainingInHand(this) > 1) {
            dealCommunityCards(1);
            bettingRound();
            collect();
        }
        showdown();
        endHand();
    }
}
