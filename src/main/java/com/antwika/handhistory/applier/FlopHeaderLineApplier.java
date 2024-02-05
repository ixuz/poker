package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.FlopHeaderLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public non-sealed class FlopHeaderLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof FlopHeaderLine flopHeaderLine)) return false;
        TableUtil.collect(tableData);
        tableData.setCards(flopHeaderLine.cards());
        tableData.setGameStage(TableData.GameStage.FLOP);
        tableData.getSeats().forEach(seat -> seat.setHasActed(false));
        tableData.getHistory().add(flopHeaderLine);
        return true;
    }
}
