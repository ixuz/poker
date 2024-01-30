package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.UncalledBetLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class UncalledBetLineParser implements ILineParser {
    final static String PATTERN = "^Uncalled bet \\((\\d+)\\) returned to (.+)$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var amount = Integer.parseInt(m.group(1));
            final var playerName = m.group(2);
            return new UncalledBetLine(amount, playerName);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof UncalledBetLine uncalledBetLine)) return false;
        final var str = String.format(
                "Uncalled bet (%d) returned to %s",
                uncalledBetLine.amount(),
                uncalledBetLine.playerName()
        );
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
