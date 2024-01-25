package com.antwika.parser.parsers;

import com.antwika.parser.lines.BlindLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class BlindLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+): posts (.+) (\\d+)").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var blindName = m.group(2);
            final var amount = Integer.parseInt(m.group(3));

            final var blindLine = new BlindLine(playerName, blindName, amount);

            // Apply
            final var optionalSeat = findSeatByPlayerName(tableData, blindLine.playerName());

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(seat.getStack() - blindLine.amount());
                seat.setCommitted(seat.getCommitted() + blindLine.amount());
            }

            return true;
        }
        return false;
    }
}
