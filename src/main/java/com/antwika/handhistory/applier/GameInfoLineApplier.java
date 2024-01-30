package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.GameInfoLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

public class GameInfoLineApplier implements ILineApplier {
    @Override
    public boolean apply(TableData tableData, ILine line) {
        if (!(line instanceof GameInfoLine gameInfoLine)) return false;
        tableData.setHandId(gameInfoLine.handId());
        tableData.setGameType(gameInfoLine.gameType());
        tableData.setSmallBlind(gameInfoLine.smallBlind());
        tableData.setBigBlind(gameInfoLine.bigBlind());
        tableData.getHistory().add(gameInfoLine);
        return true;
    }
}
