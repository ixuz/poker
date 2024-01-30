package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public class AllPlayersActedTransition extends Transition {
    public AllPlayersActedTransition(FSMState fromState, FSMState toState) {
        super("AllPlayersActedTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (TableUtil.hasAllPlayersActed(tableData)) return true;

        return false;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;
    }
}
