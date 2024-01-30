package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.RiverHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public class RiverHeaderLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof RiverHeaderLine riverHeaderLine)) return false;
        TableUtil.collect(tableData);
        tableData.setCards(riverHeaderLine.cards());
        tableData.setGameStage(TableData.GameStage.RIVER);
        tableData.getSeats().forEach(seat -> seat.setHasActed(false));
        tableData.getHistory().add(riverHeaderLine);
        return true;
    }
}
