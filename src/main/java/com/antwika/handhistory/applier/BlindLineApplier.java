package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.BlindLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public non-sealed class BlindLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof BlindLine blindLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, blindLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setStack(seat.getStack() - blindLine.amount());
            seat.setCommitted(seat.getCommitted() + blindLine.amount());
            tableData.getHistory().add(blindLine);
            return true;
        }
        return false;
    }
}
