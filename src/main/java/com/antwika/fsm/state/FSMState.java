package com.antwika.fsm.state;

import com.antwika.fsm.transition.Transition;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FSMState {
    private static final Logger logger = LoggerFactory.getLogger(FSMState.class);

    private final String name;

    private final List<Transition> transitionsIn = new ArrayList<>();

    private final List<Transition> transitionsOut = new ArrayList<>();

    public FSMState(String name) {
        this.name = name;
    }

    public void enter(Object data) {
        logger.debug("{}::enter", getName());
        onEnter(data);
    }

    public void exit(Object data) {
        logger.debug("{}::exit", getName());
        onExit(data);
    }

    public void step(Object data) {
        logger.debug("{}::step", getName());
        onStep(data);
    }

    protected void onEnter(Object data) {

    }

    protected void onExit(Object data) {

    }

    protected void onStep(Object data) {

    }
}
