package com.antwika.parser.parsers;

import com.antwika.parser.lines.HandEndLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class HandEndLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("--- HAND END ---").matcher(line);
        if (m.find()) {
            final var handEndLine = new HandEndLine();

            // Apply
            tableData.setGameStage(TableData.GameStage.NONE);
            return true;
        }
        return false;
    }
}
