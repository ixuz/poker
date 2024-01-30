package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerFoldLine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class PlayerFoldLineParser implements ILineParser {
    final static String PATTERN = "(.+): folds";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            return new PlayerFoldLine(playerName);
        }
        return null;
    }
}
