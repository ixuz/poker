package com.antwika.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandEndedState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(HandEndedState.class);

    public HandEndedState() {
        super("HandEndedState");
    }

    @Override
    protected void onEnter(Object data) {
        logger.info("--- HAND END ---");
    }

    @Override
    protected void onExit(Object data) {

    }

    @Override
    protected void onStep(Object data) {

    }
}
