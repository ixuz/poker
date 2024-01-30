package com.antwika.handhistory.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.handhistory.line.FlopHeaderLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class FlopHeaderLineParser implements ILineParser {
    private static Logger logger = LoggerFactory.getLogger(FlopHeaderLineParser.class);

    final static String PATTERN = "\\*\\*\\* FLOP \\*\\*\\* \\[(..) (..) (..)]";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var card1Notation = m.group(1);
            final var card2Notation = m.group(2);
            final var card3Notation = m.group(3);
            try {
                final var card1 = HandUtil.fromNotation(card1Notation).getBitmask();
                final var card2 = HandUtil.fromNotation(card2Notation).getBitmask();
                final var card3 = HandUtil.fromNotation(card3Notation).getBitmask();
                return new FlopHeaderLine(card1, card2, card3);
            } catch (NotationException e) {
                logger.warn("Failed to parse line", e);
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof FlopHeaderLine flopHeaderLine)) return false;
        try {
            final String str = String.format(
                    "*** FLOP *** [%s %s %s]",
                    HandUtil.toNotation(flopHeaderLine.card1()),
                    HandUtil.toNotation(flopHeaderLine.card2()),
                    HandUtil.toNotation(flopHeaderLine.card3())
            );
            baos.write(str.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (NotationException e) {
            logger.warn("Failed to write line", e);
            return false;
        }
    }
}
