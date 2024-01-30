package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerCallLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PlayerCallLineParser implements ILineParser {
    final static String PATTERN = "(.+): calls (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            return new PlayerCallLine(playerName, amount);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof PlayerCallLine playerCallLine)) return false;
        final var a = String.format(
                "%s: calls %d",
                playerCallLine.playerName(),
                playerCallLine.amount()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
