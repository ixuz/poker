package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;

public class InstantTransition extends Transition {
    public InstantTransition(FSMState fromState, FSMState toState) {
        super("InstantTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        return true;
    }

    @Override
    public void onTransition(Object data) {

    }
}
