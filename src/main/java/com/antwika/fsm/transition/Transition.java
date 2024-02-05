package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public abstract class Transition {
    private static final Logger logger = LoggerFactory.getLogger(Transition.class);

    private final String name;

    private final FSMState fromState;

    private final FSMState toState;

    public Transition(String name, FSMState fromState, FSMState toState) {
        this.name = name;
        this.fromState = fromState;
        this.toState = toState;
    }

    public void transition(Object data) {
        // logger.info("{}::transition", getName());
        onTransition(data);
    }

    abstract public boolean checkCondition(Object data);

    abstract public void onTransition(Object data);
}
