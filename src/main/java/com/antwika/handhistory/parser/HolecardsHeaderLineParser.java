package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.HolecardsHeaderLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class HolecardsHeaderLineParser implements ILineParser {
    final static String PATTERN = "\\*\\*\\* HOLE CARDS \\*\\*\\*";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new HolecardsHeaderLine();
        }
        return null;
    }
}
