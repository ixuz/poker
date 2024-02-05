package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TableInfoLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public non-sealed class TableInfoLineParser implements ILineParser {
    final static String PATTERN = "^Table '(.+)' (\\d+)-max Seat #(\\d+) is the button$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var tableName = m.group(1);
            final var seatCount = Integer.parseInt(m.group(2));
            final var buttonAt = Integer.parseInt(m.group(3));
            return new TableInfoLine(tableName, seatCount, buttonAt);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof TableInfoLine tableInfoLine)) return false;
        final var str = String.format(
                "Table '%s' %d-max Seat #%d is the button",
                tableInfoLine.tableName(),
                tableInfoLine.seatCount(),
                tableInfoLine.buttonAt()
        );
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
