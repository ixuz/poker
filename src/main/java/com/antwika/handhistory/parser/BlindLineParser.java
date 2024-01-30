package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.BlindLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class BlindLineParser implements ILineParser {
    final static String PATTERN = "(.+): posts (.+) (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var blindName = m.group(2);
            final var amount = Integer.parseInt(m.group(3));
            return new BlindLine(playerName, blindName, amount);
        }
        return null;
    }
}
