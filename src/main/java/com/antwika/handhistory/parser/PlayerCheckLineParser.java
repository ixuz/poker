package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerCheckLine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class PlayerCheckLineParser implements ILineParser {
    final static String PATTERN = "(.+): checks";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            return new PlayerCheckLine(playerName);
        }
        return null;
    }
}
