package com.antwika.parser.parsers;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.parser.lines.SummaryBoardInfoLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryBoardInfoLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Board \\[(..) (..) (..) (..) (..)]").matcher(line);
        if (m.find()) {
            try {
                final var card1 = HandUtil.fromNotation(m.group(1)).getBitmask();
                final var card2 = HandUtil.fromNotation(m.group(2)).getBitmask();
                final var card3 = HandUtil.fromNotation(m.group(3)).getBitmask();
                final var card4 = HandUtil.fromNotation(m.group(4)).getBitmask();
                final var card5 = HandUtil.fromNotation(m.group(5)).getBitmask();

                final var summaryBoardInfoLine = new SummaryBoardInfoLine(card1, card2, card3, card4, card5);

                return true;
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
