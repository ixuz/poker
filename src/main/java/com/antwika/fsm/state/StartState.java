package com.antwika.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(StartState.class);

    public StartState() {
        super("StartState");
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
