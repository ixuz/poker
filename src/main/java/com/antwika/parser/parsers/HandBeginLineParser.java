package com.antwika.parser.parsers;

import com.antwika.parser.lines.HandBeginLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class HandBeginLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("--- HAND BEGIN ---").matcher(line);
        if (m.find()) {
            final var handBeginLine = new HandBeginLine();

            // Apply
            tableData.setGameStage(TableData.GameStage.PREFLOP);
            return true;
        }
        return false;
    }
}
