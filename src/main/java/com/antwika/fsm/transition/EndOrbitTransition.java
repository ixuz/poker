package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;

public class EndOrbitTransition extends Transition {
    public EndOrbitTransition(FSMState fromState, FSMState toState) {
        super("EndOrbitTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;
        return true;
    }

    @Override
    public void onTransition(Object data) {

    }
}
