package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.BlindLine;
import com.antwika.handhistory.line.EmptyLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import static com.antwika.table.util.TableUtil.findSeatByPlayerName;

public class EmptyLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof EmptyLine emptyLine)) return false;
        tableData.getHistory().add(emptyLine);
        return true;
    }
}
