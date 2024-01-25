package com.antwika.parser.parsers;

import com.antwika.parser.lines.PlayerCheckLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class PlayerCheckLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+): checks").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);

            final var playerCheckLine = new PlayerCheckLine(playerName);

            // Apply
            final var optionalSeat = findSeatByPlayerName(tableData, playerCheckLine.playerName());

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();

                seat.setHasActed(true);

                return true;
            }
        }

        return false;
    }
}
