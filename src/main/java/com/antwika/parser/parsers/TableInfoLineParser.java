package com.antwika.parser.parsers;

import com.antwika.parser.lines.TableInfoLine;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TableInfoLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Table '(.+)' (\\d+)-max Seat #(\\d+) is the button").matcher(line);
        if (m.find()) {
            final var tableName = m.group(1);
            final var seatCount = Integer.parseInt(m.group(2));
            final var buttonAt = Integer.parseInt(m.group(3));

            final var tableInfoLine = new TableInfoLine(tableName, seatCount, buttonAt);

            final var seats = new ArrayList<SeatData>();
            for (int i = 0; i < tableInfoLine.seatCount(); i += 1) {
                final var seat = new SeatData();
                seat.setSeatIndex(i);
                seats.add(seat);
            }

            tableData.setTableName(tableInfoLine.tableName());
            tableData.setSeats(seats);
            tableData.setButtonAt(tableInfoLine.buttonAt());

            return true;
        }
        return false;
    }
}
