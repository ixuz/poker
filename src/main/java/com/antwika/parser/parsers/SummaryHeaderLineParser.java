package com.antwika.parser.parsers;

import com.antwika.parser.lines.SummaryHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class SummaryHeaderLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("\\*\\*\\* SUMMARY \\*\\*\\*").matcher(line);

        if (m.find()) {
            final var summaryHeaderLine = new SummaryHeaderLine();

            // Apply
            tableData.setGameStage(TableData.GameStage.NONE);
            tableData.getSeats().forEach(seat -> seat.setHasActed(false));
            tableData.getPots().clear();
            tableData.getSeats().forEach(seat -> seat.setCommitted(0));
            tableData.setChipsInPlay(TableUtil.countChipsInPlay(tableData));

            return true;
        }

        return false;
    }
}
