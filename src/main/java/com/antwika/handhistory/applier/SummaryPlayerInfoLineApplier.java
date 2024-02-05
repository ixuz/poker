package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryPlayerInfoLine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public non-sealed class SummaryPlayerInfoLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SummaryPlayerInfoLine summaryPlayerInfoLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, summaryPlayerInfoLine.playerName());

        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setStack(summaryPlayerInfoLine.stack());
            seat.setCommitted(0);
            tableData.getHistory().add(summaryPlayerInfoLine);
            return true;
        }
        return false;
    }
}
