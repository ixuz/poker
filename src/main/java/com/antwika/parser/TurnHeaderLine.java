package com.antwika.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class TurnHeaderLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("\\*\\*\\* TURN \\*\\*\\* \\[(..) (..) (..)] \\[(..)]").matcher(line);
        if (m.find()) {
            final var card1Notation = m.group(1);
            final var card2Notation = m.group(2);
            final var card3Notation = m.group(3);
            final var card4Notation = m.group(4);

            try {
                TableUtil.collect(tableData);

                final var card1 = HandUtil.fromNotation(card1Notation).getBitmask();
                final var card2 = HandUtil.fromNotation(card2Notation).getBitmask();
                final var card3 = HandUtil.fromNotation(card3Notation).getBitmask();
                final var card4 = HandUtil.fromNotation(card4Notation).getBitmask();
                final var cards = card1 | card2 | card3 | card4;
                tableData.setCards(cards);
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
