package com.antwika.parser;

import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TableInfoLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Table '(.+)' (\\d+)-max Seat #(\\d+) is the button").matcher(line);
        if (m.find()) {
            final var tableName = m.group(1);
            final var seatCount = m.group(2);
            final var buttonAt = m.group(3);

            final var seats = new ArrayList<SeatData>();
            for (int i = 0; i < Integer.parseInt(seatCount); i += 1) {
                final var seat = new SeatData();
                seat.setSeatIndex(i);
                seats.add(seat);
            }

            tableData.setTableName(tableName);
            tableData.setSeats(seats);
            tableData.setButtonAt(Integer.parseInt(buttonAt));

            return true;
        }
        return false;
    }
}
