package com.antwika.fsm;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public abstract class Transition {
    private final State fromState;

    private final State toState;

    public Transition(State fromState, State toState) {
        this.fromState = fromState;
        this.toState = toState;
    }

    abstract public boolean checkCondition();

    abstract public void onTransition();
}
