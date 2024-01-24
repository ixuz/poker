package com.antwika.parser;

import com.antwika.table.data.TableData;
import com.antwika.table.player.RandomPlayer;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class SeatInfoLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Seat (\\d+): (.+) \\((\\d+) in chips\\)").matcher(line);
        if (m.find()) {
            final var seatId = m.group(1);
            final var playerName = m.group(2);
            final var stack = m.group(3);

            final var player = new RandomPlayer(1L, playerName);
            final var seat = tableData.getSeats().get(Integer.parseInt(seatId) - 1);
            seat.setPlayer(player);
            seat.setStack(Integer.parseInt(stack));

            int totalChipsInPlay = TableUtil.countChipsInPlay(tableData);
            tableData.setChipsInPlay(totalChipsInPlay);

            return true;
        }
        return false;
    }
}
