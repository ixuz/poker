package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.SummaryHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public non-sealed class SummaryHeaderLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof SummaryHeaderLine summaryHeaderLine)) return false;
        tableData.setGameStage(TableData.GameStage.NONE);
        tableData.getSeats().forEach(seat -> seat.setHasActed(false));
        tableData.getPots().clear();
        tableData.getSeats().forEach(seat -> seat.setCommitted(0));
        tableData.setChipsInPlay(TableUtil.countChipsInPlay(tableData));
        tableData.setChipsInPlay(TableUtil.countChipsInPlay(tableData));
        tableData.getHistory().add(summaryHeaderLine);
        return true;
    }
}
