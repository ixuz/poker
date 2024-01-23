package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class PlayerFoldLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+): folds").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();

                seat.setHasActed(true);
                seat.setHasFolded(true);

                return true;
            }
        }
        return false;
    }
}
