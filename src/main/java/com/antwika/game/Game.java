package com.antwika.game;

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
    @Setter
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
    @Setter
    private int totalBet = 0;

    @Getter
    @Setter
    private int lastRaise = 0;

    @Getter
    @Setter
    private Long cards = 0L;

    @Getter
    private final List<Pot> pots = new ArrayList<>();

    @Getter
    private final Deck deck;

    @Getter
    private final Prng prng;

    @Getter
    private final int smallBlind;

    @Getter
    private final int bigBlind;

    @Getter
    @Setter
    private int delivered = 0;

    @Getter
    @Setter
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

    public void handleEvent(Event event) {
        if (event instanceof PlayerActionResponse e) {
            handlePlayerActionResponse(e);
        }
    }

    public void handlePlayerActionResponse(PlayerActionResponse e) {
        final Game game = e.game;
        final Seat seat = GameUtil.getSeat(this, e.player);

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
        final Seat seat = GameUtil.getSeat(this, playerActionResponse.player);
        seat.setHasFolded(true);
        logger.info("{}: folds", seat.getPlayer().getPlayerName());
    }

    public void handlePlayerCheckAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = GameUtil.getSeat(this, playerActionResponse.player);
        seat.setHasActed(true);
        logger.info("{}: checks", seat.getPlayer().getPlayerName());
    }

    public void handlePlayerBetAction(PlayerActionResponse playerActionResponse) {
        final Seat seat = GameUtil.getSeat(this, playerActionResponse.player);
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
        final Seat seat = GameUtil.getSeat(this, playerActionResponse.player);
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
        final Seat seat = GameUtil.getSeat(this, playerActionResponse.player);
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
}
