package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.HandEndLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

public class HandEndLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof HandEndLine handEndLine)) return false;
        tableData.setGameStage(TableData.GameStage.NONE);
        tableData.getHistory().add(handEndLine);
        return true;
    }
}
