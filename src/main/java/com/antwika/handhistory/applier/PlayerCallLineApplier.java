package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerCallLine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public non-sealed class PlayerCallLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof PlayerCallLine playerCallLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, playerCallLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            final var added = playerCallLine.amount() - seat.getCommitted();
            seat.setHasActed(true);
            seat.setStack(seat.getStack() - added);
            seat.setCommitted(seat.getCommitted() + added);
            tableData.getHistory().add(playerCallLine);
            return true;
        }
        return false;
    }
}
