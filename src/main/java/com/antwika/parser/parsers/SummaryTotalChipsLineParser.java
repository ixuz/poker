package com.antwika.parser.parsers;

import com.antwika.parser.lines.SummaryTotalChipsLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryTotalChipsLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Total chips in play (\\d+)").matcher(line);

        if (m.find()) {
            final var totalChipsInPlay = Integer.parseInt(m.group(1));

            final var summaryTotalChipsLine = new SummaryTotalChipsLine(totalChipsInPlay);

            // Apply
            return true;
        }

        return false;
    }
}
