package com.antwika.parser.parsers;

import com.antwika.parser.lines.CollectedPotLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class CollectedPotLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+) collected (\\d+) from (.+)").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            final var potName = m.group(3);

            final var collectedPotLine = new CollectedPotLine(playerName, amount, potName);

            // tableData.getSeats().forEach(seat -> seat.setCommitted(0));

            final var optionalSeat = findSeatByPlayerName(tableData, collectedPotLine.playerName());

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(seat.getStack() + collectedPotLine.amount());

                return true;
            }
            return true;
        }

        return false;
    }
}
