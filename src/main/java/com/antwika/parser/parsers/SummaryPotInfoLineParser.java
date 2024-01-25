package com.antwika.parser.parsers;

import com.antwika.parser.lines.SummaryPotInfoLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryPotInfoLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Total pot (\\d+)").matcher(line);

        if (m.find()) {
            final var totalPot = Integer.parseInt(m.group(1));

            final var summaryPotInfoLine = new SummaryPotInfoLine(totalPot);

            // Apply
            return true;
        }

        return false;
    }
}
