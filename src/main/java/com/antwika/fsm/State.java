package com.antwika.fsm;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class State {
    private final List<Transition> transitionsIn = new ArrayList<>();

    private final List<Transition> transitionsOut = new ArrayList<>();

    abstract public void onEnter();

    abstract public void onExit();

    abstract public void onStep();
}
