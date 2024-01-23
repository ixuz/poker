package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class SummaryPlayerInfoLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Seat (\\d+): (.+) stack (\\d+)").matcher(line);

        if (m.find()) {
            final var seatId = m.group(1);
            final var playerName = m.group(2);
            final var stack = Integer.parseInt(m.group(3));

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(stack);
                seat.setCommitted(0);

                return true;
            }
        }

        return false;
    }
}
