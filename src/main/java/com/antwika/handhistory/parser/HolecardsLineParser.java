package com.antwika.handhistory.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.handhistory.line.HolecardsLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Pattern;

public class HolecardsLineParser implements ILineParser {
    private static final Logger logger = LoggerFactory.getLogger(HolecardsLineParser.class);

    final static String PATTERN = "Dealt to (.+) \\[(..) (..)]";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var firstCardNotation = m.group(2);
            final var secondCardNotation = m.group(3);
            final var holecards = firstCardNotation.concat(secondCardNotation);
            try {
                final var handNotation = HandUtil.fromNotation(holecards);
                return new HolecardsLine(playerName, handNotation.getBitmask());
            } catch (NotationException e) {
                logger.warn("Failed to parse line", e);
                return null;
            }
        }
        return null;
    }
}
