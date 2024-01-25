package com.antwika.parser.parsers;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.parser.lines.TurnHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class TurnHeaderLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("\\*\\*\\* TURN \\*\\*\\* \\[(..) (..) (..)] \\[(..)]").matcher(line);
        if (m.find()) {
            try {
                final var card1 = HandUtil.fromNotation(m.group(1)).getBitmask();
                final var card2 = HandUtil.fromNotation(m.group(2)).getBitmask();
                final var card3 = HandUtil.fromNotation(m.group(3)).getBitmask();
                final var card4 = HandUtil.fromNotation(m.group(4)).getBitmask();

                final var turnHeaderLine = new TurnHeaderLine(card1, card2, card3, card4);

                // Apply
                TableUtil.collect(tableData);
                tableData.setCards(turnHeaderLine.cards());
                tableData.setGameStage(TableData.GameStage.TURN);
                tableData.getSeats().forEach(seat -> seat.setHasActed(false));

                return true;
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
