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

        final var tableData = new TableData();
        tableData.setTableName(tableName);
        tableData.setGameType("Hold'em No Limit");
        tableData.setGameStage(TableData.GameStage.NONE);
        tableData.setSeats(seats);
        tableData.setDeckData(new DeckData(prng.nextInt()));
        tableData.setSmallBlind(smallBlind);
        tableData.setBigBlind(bigBlind);
        tableData.setHandId(0L);
        tableData.setButtonAt(0);
        tableData.setActionAt(0);
        tableData.setTotalBet(0);
        tableData.setLastRaise(0);
        tableData.setCards(0L);
        tableData.setDelivered(0);
        tableData.setChipsInPlay(0);
        tableData.setCards(0L);

        return tableData;
    }
}
