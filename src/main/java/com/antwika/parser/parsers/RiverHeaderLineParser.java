package com.antwika.parser.parsers;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.parser.lines.RiverHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class RiverHeaderLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("\\*\\*\\* RIVER \\*\\*\\* \\[(..) (..) (..) (..)] \\[(..)]").matcher(line);
        if (m.find()) {
            try {
                final var card1 = HandUtil.fromNotation(m.group(1)).getBitmask();
                final var card2 = HandUtil.fromNotation(m.group(2)).getBitmask();
                final var card3 = HandUtil.fromNotation(m.group(3)).getBitmask();
                final var card4 = HandUtil.fromNotation(m.group(4)).getBitmask();
                final var card5 = HandUtil.fromNotation(m.group(5)).getBitmask();

                final var riverHeaderLine = new RiverHeaderLine(card1, card2, card3, card4, card5);

                // Apply
                TableUtil.collect(tableData);
                tableData.setCards(riverHeaderLine.cards());
                tableData.setGameStage(TableData.GameStage.RIVER);
                tableData.getSeats().forEach(seat -> seat.setHasActed(false));

                return true;
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
