package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.CollectedPotLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public class CollectedPotLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof CollectedPotLine collectedPotLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, collectedPotLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setStack(seat.getStack() + collectedPotLine.amount());
            tableData.getHistory().add(collectedPotLine);
            return true;
        }
        return false;
    }
}
