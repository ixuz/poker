package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public class CollectChipsTransition extends Transition {
    public CollectChipsTransition(FSMState fromState, FSMState toState) {
        super("CollectChipsTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (!TableUtil.hasAllPlayersActed(tableData)) return false;
        if (!TableUtil.anyPlayerHasCommittedChips(tableData)) return false;
        if (tableData.getGameStage() == TableData.GameStage.NONE) return false;
        if (tableData.getGameStage() == TableData.GameStage.SHOWDOWN) return false;
        return tableData.getGameStage() != TableData.GameStage.HAND_BEGUN;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;

        TableUtil.collect(tableData);
    }
}
