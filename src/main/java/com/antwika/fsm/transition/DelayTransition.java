package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;

public class DelayTransition extends Transition {
    private final long delayUntil;

    public DelayTransition(FSMState fromState, FSMState toState, long delayMillis) {
        super("DelayTransition", fromState, toState);
        this.delayUntil = System.currentTimeMillis() + delayMillis;
    }

    @Override
    public boolean checkCondition(Object data) {
        return System.currentTimeMillis() >= delayUntil;
    }

    @Override
    public void onTransition(Object data) {

    }
}
