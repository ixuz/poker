package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.HolecardsLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public class HolecardsLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof HolecardsLine holecardsLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, holecardsLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setCards(holecardsLine.holecards());
            tableData.getHistory().add(holecardsLine);
            return true;
        }
        return false;
    }
}
