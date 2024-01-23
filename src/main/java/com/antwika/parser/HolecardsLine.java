package com.antwika.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class HolecardsLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Dealt to (.+) \\[(..) (..)]").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var firstCardNotation = m.group(2);
            final var secondCardNotation = m.group(3);

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                final var holecards = firstCardNotation.concat(secondCardNotation);

                try {
                    final var handNotation = HandUtil.fromNotation(holecards);
                    seat.setCards(handNotation.getBitmask());
                } catch (NotationException e) {
                    throw new RuntimeException(e);
                }

                return true;
            }
        }

        return false;
    }
}
