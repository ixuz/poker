package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TotalPotLine;
import com.antwika.table.data.TableData;

public class TotalPotLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof TotalPotLine totalPotLine)) return false;
        tableData.getHistory().add(totalPotLine);
        return true;
    }
}
