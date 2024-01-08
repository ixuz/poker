package com.antwika.game;

import com.antwika.game.common.Prng;
import com.antwika.game.data.DeckData;
import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;

import java.util.ArrayList;
import java.util.List;

public class GameDataFactory {
    public static GameData createGameData(long prngSeed, String tableName, int seatCount, int smallBlind, int bigBlind) {
        final Prng prng = new Prng(prngSeed);

        final List<SeatData> seats = new ArrayList<>();
        for (int i = 0; i < seatCount; i += 1) {
            final SeatData seat = new SeatData();
            seat.setSeatIndex(i);
            seats.add(seat);
        }

        return GameData.builder()
                .tableName(tableName)
                .gameType("Hold'em No Limit")
                .gameStage(GameData.GameStage.NONE)
                .seats(seats)
                .deckData(new DeckData(prng.nextInt()))
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
                .prng(prng)
                .cards(0L)
                .build();
    }
}
