package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryTotalChipsLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SummaryTotalChipsLineParser implements ILineParser {
    final static String PATTERN = "Total chips in play (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var totalChipsInPlay = Integer.parseInt(m.group(1));
            return new SummaryTotalChipsLine(totalChipsInPlay);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof SummaryTotalChipsLine summaryTotalChipsLine)) return false;
        final var str = String.format(
                "Total chips in play %d",
                summaryTotalChipsLine.totalChipsInPlay()
        );
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
