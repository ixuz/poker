package com.antwika.fsm.transition;

import com.antwika.common.exception.NotationException;
import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowdownTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(ShowdownTransition.class);

    public ShowdownTransition(FSMState fromState, FSMState toState) {
        super("ShowdownTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (tableData.getGameStage() != TableData.GameStage.RIVER) return false;
        if (TableUtil.countPlayersRemainingInHand(tableData) <= 1) return false;
        if (TableUtil.numberOfPlayersWithNonZeroStack(tableData) > 1) return false;

        return true;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;

        logger.info("Showdown");
        try {
            TableUtil.deliverWinnings(tableData);
            tableData.setGameStage(TableData.GameStage.NONE);
            TableUtil.prepareHand(tableData);
            TableUtil.resetAllSeats(tableData);
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }
}
