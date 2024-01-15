package com.antwika.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartState extends State {
    private static final Logger logger = LoggerFactory.getLogger(StartState.class);

    @Override
    public void onEnter() {
        logger.info("Entered!");
    }

    @Override
    public void onExit() {
        logger.info("Exited!");
    }

    @Override
    public void onStep() {
        logger.info("Step!");
    }
}
