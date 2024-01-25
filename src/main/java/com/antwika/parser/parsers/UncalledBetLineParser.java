package com.antwika.parser.parsers;

import com.antwika.parser.lines.UncalledBetLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class UncalledBetLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Uncalled bet \\((\\d+)\\) returned to (.+)").matcher(line);
        if (m.find()) {
            final var amount = Integer.parseInt(m.group(1));
            final var playerName = m.group(2);

            final var uncalledBetLine = new UncalledBetLine(amount, playerName);

            // Apply
            final var optionalSeat = findSeatByPlayerName(tableData, uncalledBetLine.playerName());

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(seat.getStack() + uncalledBetLine.amount());
                seat.setCommitted(seat.getCommitted() - uncalledBetLine.amount());

                return true;
            }
        }

        return false;
    }
}
