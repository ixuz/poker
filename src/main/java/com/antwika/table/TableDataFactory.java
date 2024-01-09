package com.antwika.table;

import com.antwika.table.common.Prng;
import com.antwika.table.data.DeckData;
import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;

import java.util.ArrayList;
import java.util.List;

public class TableDataFactory {
    public static TableData createTableData(long prngSeed, String tableName, int seatCount, int smallBlind, int bigBlind) {
        final Prng prng = new Prng(prngSeed);

        final List<SeatData> seats = new ArrayList<>();
        for (int i = 0; i < seatCount; i += 1) {
            final SeatData seat = new SeatData();
            seat.setSeatIndex(i);
            seats.add(seat);
        }

        return TableData.builder()
                .tableName(tableName)
                .gameType("Hold'em No Limit")
                .gameStage(TableData.GameStage.NONE)
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
