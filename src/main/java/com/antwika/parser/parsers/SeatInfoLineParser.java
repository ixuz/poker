package com.antwika.parser.parsers;

import com.antwika.parser.lines.SeatInfoLine;
import com.antwika.table.data.TableData;
import com.antwika.table.player.RandomPlayer;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class SeatInfoLineParser implements ILineParser {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Seat (\\d+): (.+) \\((\\d+) in chips\\)").matcher(line);
        if (m.find()) {
            final var seatId = Integer.parseInt(m.group(1));
            final var playerName = m.group(2);
            final var stack = Integer.parseInt(m.group(3));

            final var seatInfoLine = new SeatInfoLine(seatId, playerName, stack);

            // Apply
            final var player = new RandomPlayer(1L, seatInfoLine.playerName());
            final var seat = tableData.getSeats().get(seatInfoLine.seatId() - 1);
            seat.setPlayer(player);
            seat.setStack(seatInfoLine.stack());

            int totalChipsInPlay = TableUtil.countChipsInPlay(tableData);
            tableData.setChipsInPlay(totalChipsInPlay);

            return true;
        }
        return false;
    }
}
