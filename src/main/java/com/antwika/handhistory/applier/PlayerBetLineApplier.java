package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.PlayerBetLine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public non-sealed class PlayerBetLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof PlayerBetLine playerBetLine)) return false;
        final var optionalSeat = findSeatByPlayerName(tableData, playerBetLine.playerName());
        if (optionalSeat.isPresent()) {
            final var seat = optionalSeat.get();
            final var added = playerBetLine.amount() - seat.getCommitted();
            seat.setHasActed(true);
            seat.setStack(seat.getStack() - added);
            seat.setCommitted(seat.getCommitted() + added);
            tableData.getHistory().add(playerBetLine);
            return true;
        }
        return false;
    }
}
