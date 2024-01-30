package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.HandEndLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class HandEndLineParser implements ILineParser {
    final static String PATTERN = "--- HAND END ---";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new HandEndLine();
        }
        return null;
    }
}
