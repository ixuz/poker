package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(DelayTransition.class);

    private final long delayUntil;

    public DelayTransition(FSMState fromState, FSMState toState, long delayMillis) {
        super("DelayTransition", fromState, toState);
        this.delayUntil = System.currentTimeMillis() + delayMillis;
    }

    @Override
    public boolean checkCondition(Object data) {
        if (System.currentTimeMillis() >= delayUntil) {
            return true;
        }

        return false;
    }

    @Override
    public void onTransition(Object data) {

    }
}
