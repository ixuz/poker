package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerFoldLine;
import com.antwika.table.data.TableData;
import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public class PlayerFoldLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof PlayerFoldLine playerFoldLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, playerFoldLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            seat.setHasActed(true);
            seat.setHasFolded(true);
            tableData.getHistory().add(playerFoldLine);
            return true;
        }
        return false;
    }
}
