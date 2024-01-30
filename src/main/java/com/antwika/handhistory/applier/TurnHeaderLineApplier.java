package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.handhistory.line.TurnHeaderLine;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public class TurnHeaderLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof TurnHeaderLine turnHeaderLine)) return false;
        TableUtil.collect(tableData);
        tableData.setCards(turnHeaderLine.cards());
        tableData.setGameStage(TableData.GameStage.TURN);
        tableData.getSeats().forEach(seat -> seat.setHasActed(false));
        tableData.getHistory().add(turnHeaderLine);
        return true;
    }
}
