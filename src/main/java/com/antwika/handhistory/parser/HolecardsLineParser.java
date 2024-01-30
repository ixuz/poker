package com.antwika.handhistory.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.handhistory.line.HolecardsLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HolecardsLineParser implements ILineParser {
    private static final Logger logger = LoggerFactory.getLogger(HolecardsLineParser.class);

    final static String PATTERN = "^Dealt to (.+) \\[(..) (..)]$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            try {
                final var card1 = HandUtil.fromNotation(m.group(2)).getBitmask();
                final var card2 = HandUtil.fromNotation(m.group(3)).getBitmask();
                return new HolecardsLine(playerName, card1, card2);
            } catch (NotationException e) {
                logger.warn("Failed to parse line", e);
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof HolecardsLine holecardsLine)) return false;
        try {
            final String str = String.format(
                    "Dealt to %s [%s %s]",
                    holecardsLine.playerName(),
                    HandUtil.toNotation(holecardsLine.card1()),
                    HandUtil.toNotation(holecardsLine.card2())
            );
            baos.write(str.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (NotationException e) {
            logger.warn("Failed to write line", e);
            return false;
        }
    }
}
