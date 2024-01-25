package com.antwika.parser.parsers;

import com.antwika.parser.lines.PlayerBetLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class PlayerBetLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+): bets (\\d+)").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));

            final var playerBetLine = new PlayerBetLine(playerName, amount);

            // Apply
            final var optionalSeat = findSeatByPlayerName(tableData, playerBetLine.playerName());

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                final var added = playerBetLine.amount() - seat.getCommitted();

                seat.setHasActed(true);
                seat.setStack(seat.getStack() - added);
                seat.setCommitted(seat.getCommitted() + added);

                return true;
            }
        }
        return false;
    }
}
