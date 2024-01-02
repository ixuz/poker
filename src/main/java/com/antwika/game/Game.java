package com.antwika.game;

import com.antwika.game.handler.ActionHandler;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    @Getter
    private final Prng prng;

    @Getter
    private final GameData gameData;

    private final ActionHandler actionHandler = new ActionHandler();

    public Game(long prngSeed, String tableName, int seatCount, int smallBlind, int bigBlind) {
        this.prng = new Prng(prngSeed);

        final List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < seatCount; i += 1) {
            final Seat seat = new Seat();
            seat.setSeatIndex(i);
            seats.add(seat);
        }

        gameData = GameData.builder()
                .tableName(tableName)
                .seats(seats)
                .deck(new Deck(prng.nextInt()))
                .smallBlind(smallBlind)
                .bigBlind(bigBlind)
                .handId(0L)
                .buttonAt(0)
                .actionAt(0)
                .totalBet(0)
                .lastRaise(0)
                .cards(0L)
                .delivered(0)
                .chipsInPlay(0)
                .build();
    }

    public void handleEvent(Event event) {
        actionHandler.handleEvent(event);
    }
}
