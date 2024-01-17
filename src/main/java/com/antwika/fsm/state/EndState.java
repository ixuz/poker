package com.antwika.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(EndState.class);

    public EndState() {
        super("EndState");
    }

    @Override
    protected void onEnter(Object data) {

    }

    @Override
    protected void onExit(Object data) {

    }

    @Override
    protected void onStep(Object data) {

    }
}
