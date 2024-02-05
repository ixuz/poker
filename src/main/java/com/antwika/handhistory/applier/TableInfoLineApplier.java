package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TableInfoLine;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import java.util.ArrayList;

public non-sealed class TableInfoLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof TableInfoLine tableInfoLine)) return false;
        final var seats = new ArrayList<SeatData>();
        for (var i = 0; i < tableInfoLine.seatCount(); i += 1) {
            final var seat = new SeatData();
            seat.setSeatIndex(i);
            seats.add(seat);
        }
        tableData.setTableName(tableInfoLine.tableName());
        tableData.setSeats(seats);
        tableData.setButtonAt(tableInfoLine.buttonAt());
        tableData.getHistory().add(tableInfoLine);
        return true;
    }
}
