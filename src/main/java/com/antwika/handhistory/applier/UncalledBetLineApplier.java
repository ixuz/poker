package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.UncalledBetLine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public class UncalledBetLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof UncalledBetLine uncalledBetLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, uncalledBetLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setStack(seat.getStack() + uncalledBetLine.amount());
            seat.setCommitted(seat.getCommitted() - uncalledBetLine.amount());
            tableData.getHistory().add(uncalledBetLine);
            return true;
        }
        return false;
    }
}
