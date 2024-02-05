package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

public class CanAnyPlayerActTransition extends Transition {
    public CanAnyPlayerActTransition(FSMState fromState, FSMState toState) {
        super("CanAnyPlayerActTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;
        return !TableUtil.hasAllPlayersActed(tableData);
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;
    }
}
