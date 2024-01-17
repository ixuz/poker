package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectChipsTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(CollectChipsTransition.class);

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
        if (tableData.getGameStage() == TableData.GameStage.HAND_BEGUN) return false;

        return true;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;

        TableUtil.collect(tableData);
    }
}
