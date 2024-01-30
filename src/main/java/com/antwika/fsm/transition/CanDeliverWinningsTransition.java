package com.antwika.fsm.transition;

import com.antwika.common.exception.NotationException;
import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CanDeliverWinningsTransition extends Transition {
    public CanDeliverWinningsTransition(FSMState fromState, FSMState toState) {
        super("CanDeliverWinningsTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        final int numberOfPlayersWithNonFoldedAndNonZeroStack = TableUtil.numberOfPlayersWithNonFoldedAndNonZeroStack(tableData);
        final int countPlayersRemainingInHand = TableUtil.countPlayersRemainingInHand(tableData);

        if (numberOfPlayersWithNonFoldedAndNonZeroStack < 2 && countPlayersRemainingInHand < 2) {
            return true;
        }

        if (!TableUtil.hasAllPlayersActed(tableData)) return false;

        return true;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;

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
