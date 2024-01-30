package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterTransition extends Transition {
    private final int countTo;

    private int counter = 0;

    public CounterTransition(FSMState fromState, FSMState toState, int countTo) {
        super("CounterTransition", fromState, toState);
        this.countTo = countTo;
    }

    @Override
    public boolean checkCondition(Object data) {
        if (counter >= countTo) {
            return true;
        }

        counter++;

        return false;
    }

    @Override
    public void onTransition(Object data) {
        counter = 0;
    }
}
