package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.CollectedPotLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class CollectedPotLineParser implements ILineParser {
    final static String PATTERN = "^(.+) collected (\\d+) from (.+)$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            final var potName = m.group(3);
            return new CollectedPotLine(playerName, amount, potName);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof CollectedPotLine collectedPotLine)) return false;
        final var a = String.format(
                "%s collected %d from %s",
                collectedPotLine.playerName(),
                collectedPotLine.amount(),
                collectedPotLine.potName()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
