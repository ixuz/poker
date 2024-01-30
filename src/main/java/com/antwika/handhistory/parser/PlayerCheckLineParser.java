package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerCheckLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PlayerCheckLineParser implements ILineParser {
    final static String PATTERN = "(.+): checks";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            return new PlayerCheckLine(playerName);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof PlayerCheckLine playerCheckLine)) return false;
        final var a = String.format(
                "%s: checks",
                playerCheckLine.playerName()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
