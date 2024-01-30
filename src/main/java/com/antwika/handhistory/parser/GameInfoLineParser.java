package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.GameInfoLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class GameInfoLineParser implements ILineParser {
    final static String PATTERN = "^Poker Hand #(\\d+): (.+) \\((\\d+)/(\\d+)\\) - (.*)$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var handId = m.group(1);
            final var gameType = m.group(2);
            final var smallBlind = m.group(3);
            final var bigBlind = m.group(4);
            final var timestamp = m.group(5);
            return new GameInfoLine(
                    Integer.parseInt(handId),
                    gameType,
                    Integer.parseInt(smallBlind),
                    Integer.parseInt(bigBlind),
                    timestamp
            );
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof GameInfoLine gameInfoLine)) return false;
        final var a = String.format(
                "Poker Hand #%d: %s (%d/%d) - %s",
                gameInfoLine.handId(),
                gameInfoLine.gameType(),
                gameInfoLine.smallBlind(),
                gameInfoLine.bigBlind(),
                gameInfoLine.timestamp()
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
