package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TotalPotLine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class TotalPotLineParser implements ILineParser {
    final static String PATTERN = "Total pot: (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var totalPot = Integer.parseInt(m.group(1));
            return new TotalPotLine(totalPot);
        }
        return null;
    }
}
