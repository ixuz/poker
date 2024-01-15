package com.antwika.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstantTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(InstantTransition.class);

    public InstantTransition(State fromState, State toState) {
        super(fromState, toState);
    }

    @Override
    public boolean checkCondition() {
        return true;
    }

    @Override
    public void onTransition() {

    }
}
