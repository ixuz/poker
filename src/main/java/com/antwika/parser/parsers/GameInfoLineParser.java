package com.antwika.parser.parsers;

import com.antwika.parser.lines.GameInfoLine;
import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class GameInfoLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Poker Hand #(\\d+): (.+) \\((\\d+)/(\\d+)\\) - (.*)").matcher(line);
        if (m.find()) {
            final var handId = m.group(1);
            final var gameType = m.group(2);
            final var smallBlind = m.group(3);
            final var bigBlind = m.group(4);
            final var timestamp = m.group(5);

            final var gameInfoLine = new GameInfoLine(
                    Integer.parseInt(handId),
                    gameType,
                    Integer.parseInt(smallBlind),
                    Integer.parseInt(bigBlind),
                    timestamp
            );

            // Apply
            tableData.setHandId(gameInfoLine.handId());
            tableData.setGameType(gameInfoLine.gameType());
            tableData.setSmallBlind(gameInfoLine.smallBlind());
            tableData.setBigBlind(gameInfoLine.bigBlind());
            return true;
        }
        return false;
    }
}
