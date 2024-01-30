package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryPlayerInfoLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SummaryPlayerInfoLineParser implements ILineParser {
    final static String PATTERN = "Seat (\\d+): (.+) stack (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var seatId = Integer.parseInt(m.group(1));
            final var playerName = m.group(2);
            final var stack = Integer.parseInt(m.group(3));
            return new SummaryPlayerInfoLine(seatId, playerName, stack);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof SummaryPlayerInfoLine summaryPlayerInfoLine)) return false;
        final var str = String.format(
                "Seat %d: %s stack %d",
                summaryPlayerInfoLine.seatId(),
                summaryPlayerInfoLine.playerName(),
                summaryPlayerInfoLine.stack()
        );
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
