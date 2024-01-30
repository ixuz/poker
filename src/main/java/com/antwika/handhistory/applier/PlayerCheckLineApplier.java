package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerCheckLine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public class PlayerCheckLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof PlayerCheckLine playerCheckLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, playerCheckLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setHasActed(true);
            tableData.getHistory().add(playerCheckLine);
            return true;
        }
        return false;
    }
}
