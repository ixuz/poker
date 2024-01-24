package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class BlindLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+): posts (.+) (\\d+)").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var blindName = m.group(2);
            final var amount = Integer.parseInt(m.group(3));

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(seat.getStack() - amount);
                seat.setCommitted(seat.getCommitted() + amount);
            }

            return true;
        }
        return false;
    }
}