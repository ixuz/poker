package com.antwika.handhistory.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TurnHeaderLine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TurnHeaderLineParser implements ILineParser {
    private static final Logger logger = LoggerFactory.getLogger(TurnHeaderLineParser.class);

    final static String PATTERN = "^\\*\\*\\* TURN \\*\\*\\* \\[(..) (..) (..)] \\[(..)]$";

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

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof TurnHeaderLine turnHeaderLine)) return false;
        try {
            final String str = String.format(
                    "*** TURN *** [%s %s %s] [%s]",
                    HandUtil.toNotation(turnHeaderLine.card1()),
                    HandUtil.toNotation(turnHeaderLine.card2()),
                    HandUtil.toNotation(turnHeaderLine.card3()),
                    HandUtil.toNotation(turnHeaderLine.card4())
            );
            baos.write(str.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (NotationException e) {
            logger.warn("Failed to write line", e);
            return false;
        }
    }
}
