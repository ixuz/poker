package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SeatInfoLine;
import com.antwika.table.data.TableData;
import com.antwika.table.player.RandomPlayer;
import com.antwika.table.util.TableUtil;

public non-sealed class SeatInfoLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SeatInfoLine seatInfoLine)) return false;
        final var player = new RandomPlayer(1L, seatInfoLine.playerName());
        final var seat = tableData.getSeats().get(seatInfoLine.seatId() - 1);
        seat.setPlayer(player);
        seat.setStack(seatInfoLine.stack());
        int totalChipsInPlay = TableUtil.countChipsInPlay(tableData);
        tableData.setChipsInPlay(totalChipsInPlay);
        tableData.getHistory().add(seatInfoLine);
        return true;
    }
}
