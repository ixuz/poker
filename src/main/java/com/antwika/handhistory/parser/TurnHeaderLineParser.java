package com.antwika.handhistory.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TurnHeaderLine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Pattern;

public class TurnHeaderLineParser implements ILineParser {
    private static final Logger logger = LoggerFactory.getLogger(TurnHeaderLineParser.class);

    final static String PATTERN = "\\*\\*\\* TURN \\*\\*\\* \\[(..) (..) (..)] \\[(..)]";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            try {
                final var card1 = HandUtil.fromNotation(m.group(1)).getBitmask();
                final var card2 = HandUtil.fromNotation(m.group(2)).getBitmask();
                final var card3 = HandUtil.fromNotation(m.group(3)).getBitmask();
                final var card4 = HandUtil.fromNotation(m.group(4)).getBitmask();
                return new TurnHeaderLine(card1, card2, card3, card4);
            } catch (NotationException e) {
                logger.warn("Failed to parse line", e);
                return null;
            }
        }
        return null;
    }
}
