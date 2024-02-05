package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerRaiseLine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public non-sealed class PlayerRaiseLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof PlayerRaiseLine playerRaiseLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, playerRaiseLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            final var added = playerRaiseLine.amount() - seat.getCommitted();
            seat.setHasActed(true);
            seat.setStack(seat.getStack() - added);
            seat.setCommitted(seat.getCommitted() + added);
            tableData.getHistory().add(playerRaiseLine);
            return true;
        }
        return false;
    }
}
