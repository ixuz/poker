package com.antwika.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(CounterTransition.class);

    private final int countTo;

    private int counter = 0;

    public CounterTransition(State fromState, State toState, int countTo) {
        super(fromState, toState);
        this.countTo = countTo;
    }

    @Override
    public boolean checkCondition() {
        if (counter >= countTo) {
            return true;
        }

        counter++;

        return false;
    }

    @Override
    public void onTransition() {
        counter = 0;
    }
}
