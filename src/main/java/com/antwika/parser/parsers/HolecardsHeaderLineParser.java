package com.antwika.parser.parsers;

import com.antwika.parser.lines.HolecardsHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class HolecardsHeaderLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("\\*\\*\\* HOLE CARDS \\*\\*\\*").matcher(line);

        if (m.find()) {
            final var holecardsHeaderLine = new HolecardsHeaderLine();

            // Apply
            int totalChipsInPlay = TableUtil.countChipsInPlay(tableData);
            tableData.setChipsInPlay(totalChipsInPlay);

            return true;
        }
        return false;
    }
}
