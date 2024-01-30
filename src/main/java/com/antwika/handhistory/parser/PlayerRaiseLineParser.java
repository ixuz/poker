package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerRaiseLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PlayerRaiseLineParser implements ILineParser {
    final static String PATTERN = "(.+): raises to (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            return new PlayerRaiseLine(playerName, amount);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof PlayerRaiseLine playerRaiseLine)) return false;
        final var str = String.format(
                "%s: raises to %s",
                playerRaiseLine.playerName(),
                playerRaiseLine.amount()
        );
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
