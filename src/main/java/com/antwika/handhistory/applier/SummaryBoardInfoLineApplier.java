package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryBoardInfoLine;
import com.antwika.table.data.TableData;

public class SummaryBoardInfoLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SummaryBoardInfoLine summaryBoardInfoLine)) return false;
        tableData.getHistory().add(summaryBoardInfoLine);
        return true;
    }
}
