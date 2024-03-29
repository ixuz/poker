package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryTotalChipsLine;
import com.antwika.table.data.TableData;

public non-sealed class SummaryTotalChipsLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SummaryTotalChipsLine summaryTotalChipsLine)) return false;
        tableData.setChipsInPlay(summaryTotalChipsLine.totalChipsInPlay());
        tableData.getHistory().add(summaryTotalChipsLine);
        return true;
    }
}
