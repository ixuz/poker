package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.HolecardsHeaderLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public class HolecardsHeaderLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof HolecardsHeaderLine holecardsHeaderLine)) return false;
        int totalChipsInPlay = TableUtil.countChipsInPlay(tableData);
        tableData.setChipsInPlay(totalChipsInPlay);
        tableData.getHistory().add(holecardsHeaderLine);
        return true;
    }
}
