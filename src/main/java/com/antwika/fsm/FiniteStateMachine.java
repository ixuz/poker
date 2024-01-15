package com.antwika.fsm;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class FiniteStateMachine {
    private static final Logger logger = LoggerFactory.getLogger(FiniteStateMachine.class);

    private final List<State> states = new ArrayList<>();

    private final List<Transition> transitions = new ArrayList<>();

    private final State startState;

    private final State endState;

    private State currentState;

    private long step = 0L;

    public FiniteStateMachine() {
        startState = new StartState();
        addState(startState);

        endState = new EndState();
        addState(endState);

        currentState = startState;
    }

    public void addState(State state) {
        states.add(state);
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);

        final Optional<State> fromState = states.stream()
                .filter(state -> state.equals(transition.getFromState()))
                .findFirst();

        final Optional<State> toState = states.stream()
                .filter(state -> state.equals(transition.getToState()))
                .findFirst();

        if (fromState.isPresent() && toState.isPresent()) {
            fromState.get().getTransitionsOut().add(transition);
            toState.get().getTransitionsIn().add(transition);
        }
    }

    public boolean isEndStageReached() {
        return getCurrentState().equals(getEndState());
    }

    public void step() {
        boolean transitioned = false;

        for (Transition transition : getCurrentState().getTransitionsOut()) {
            if (transition.checkCondition()) {
                final State fromState = getCurrentState();
                final State toState = transition.getToState();

                currentState = toState;

                fromState.onExit();
                transition.onTransition();
                toState.onEnter();

                transitioned = true;

                break;
            }
        }

        if (!transitioned) {
            final State currentState = getCurrentState();
            currentState.onStep();
        }

        step++;
    }
}
