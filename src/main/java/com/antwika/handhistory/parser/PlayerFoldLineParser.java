package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerFoldLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public non-sealed class PlayerFoldLineParser implements ILineParser {
    final static String PATTERN = "^(.+): folds$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            return new PlayerFoldLine(playerName);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof PlayerFoldLine playerFoldLine)) return false;
        final var a = String.format(
                "%s: folds",
                playerFoldLine.playerName()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
