package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class GameInfoLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Poker Hand #(\\d+): (.+) \\((\\d+)/(\\d+)\\) - (.*)").matcher(line);
        if (m.find()) {
            final var handId = m.group(1);
            final var gameType = m.group(2);
            final var smallBlind = m.group(3);
            final var bigBlind = m.group(4);
            final var timestamp = m.group(5);
            tableData.setHandId(Integer.parseInt(handId));
            tableData.setGameType(gameType);
            tableData.setSmallBlind(Integer.parseInt(smallBlind));
            tableData.setBigBlind(Integer.parseInt(bigBlind));
            return true;
        }
        return false;
    }
}
