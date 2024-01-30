package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerBetLine;
import com.antwika.table.data.TableData;
import java.util.regex.Pattern;

public class PlayerBetLineParser implements ILineParser {
    final static String PATTERN = "(.+): bets (\\d+)";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            return new PlayerBetLine(playerName, amount);
        }
        return null;
    }
}
