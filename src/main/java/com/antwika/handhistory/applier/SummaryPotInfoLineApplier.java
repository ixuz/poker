package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryPotInfoLine;
import com.antwika.table.data.TableData;

public class SummaryPotInfoLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SummaryPotInfoLine summaryPotInfoLine)) return false;
        tableData.getHistory().add(summaryPotInfoLine);
        return true;
    }
}
