package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.HandBeginLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class HandBeginLineParser implements ILineParser {
    final static String PATTERN = "--- HAND BEGIN ---";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new HandBeginLine();
        }
        return null;
    }
}
