package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SeatInfoLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SeatInfoLineParser implements ILineParser {
    final static String PATTERN = "Seat (\\d+): (.+) \\((\\d+) in chips\\)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var seatId = Integer.parseInt(m.group(1));
            final var playerName = m.group(2);
            final var stack = Integer.parseInt(m.group(3));
            return new SeatInfoLine(seatId, playerName, stack);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof SeatInfoLine seatInfoLine)) return false;
        final var a = String.format(
                "Seat %d: %s (%d in chips)",
                seatInfoLine.seatId(),
                seatInfoLine.playerName(),
                seatInfoLine.stack()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
