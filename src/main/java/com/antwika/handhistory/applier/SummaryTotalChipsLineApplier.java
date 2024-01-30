package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryTotalChipsLine;
import com.antwika.table.data.TableData;

public class SummaryTotalChipsLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SummaryTotalChipsLine summaryTotalChipsLine)) return false;
        tableData.getHistory().add(summaryTotalChipsLine);
        return true;
    }
}
