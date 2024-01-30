package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.CollectedPotLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class CollectedPotLineParser implements ILineParser {
    final static String PATTERN = "(.+) collected (\\d+) from (.+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            final var potName = m.group(3);
            return new CollectedPotLine(playerName, amount, potName);
        }
        return null;
    }
}
