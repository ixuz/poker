package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.HandBeginLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

public non-sealed class HandBeginLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof HandBeginLine handBeginLine)) return false;
        tableData.setGameStage(TableData.GameStage.PREFLOP);
        tableData.getHistory().add(handBeginLine);
        return true;
    }
}
