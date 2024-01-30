package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerBetLine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class PlayerBetLineParser implements ILineParser {
    final static String PATTERN = "(.+): bets (\\d+)( and is all-in)?";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            final var allIn = m.group(3) != null;
            return new PlayerBetLine(playerName, amount, allIn);
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof PlayerBetLine playerBetLine)) return false;
        final var a = String.format(
                "%s: bets %d",
                playerBetLine.playerName(),
                playerBetLine.amount()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));

        if (playerBetLine.allIn()) {
            baos.write(" and is all-in".getBytes(StandardCharsets.UTF_8));
        }
        return true;
    }
}
