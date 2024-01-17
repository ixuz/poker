package com.antwika.fsm;

import com.antwika.fsm.state.EndState;
import com.antwika.fsm.state.StartState;
import com.antwika.fsm.state.FSMState;
import com.antwika.fsm.transition.Transition;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class FiniteStateMachine extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(FiniteStateMachine.class);

    private final List<FSMState> states = new ArrayList<>();

    private final List<Transition> transitions = new ArrayList<>();

    private final FSMState startState;

    private final FSMState endState;

    private FSMState currentState;

    private final Object data;

    private long stepsSinceLastTransition = 0L;

    private final float stepRate;

    private boolean running = false;

    public FiniteStateMachine(Object data, float stepRate) {
        startState = new StartState();
        addState(startState);

        endState = new EndState();
        addState(endState);

        currentState = startState;

        this.data = data;
        this.stepRate = stepRate;
    }

    @Override
    public void run() {
        try {
            running = true;
            while (running) {
                if (isEndStageReached()) {
                    running = false;
                }

                step();
                long sleepMillis = (long) (1000f/stepRate);
                Thread.sleep(sleepMillis);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addState(FSMState state) {
        states.add(state);
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);

        final Optional<FSMState> fromState = states.stream()
                .filter(state -> state.equals(transition.getFromState()))
                .findFirst();

        final Optional<FSMState> toState = states.stream()
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

        if (stepsSinceLastTransition > 0L) {
            for (Transition transition : getCurrentState().getTransitionsOut()) {
                if (transition.checkCondition(getData())) {
                    final FSMState fromState = getCurrentState();
                    final FSMState toState = transition.getToState();

                    currentState = toState;

                    fromState.exit(getData());
                    transition.transition(getData());
                    toState.enter(getData());

                    transitioned = true;

                    break;
                }
            }
        }

        if (transitioned) {
            stepsSinceLastTransition = 0L;
        } else {
            stepsSinceLastTransition++;
            final FSMState currentState = getCurrentState();
            currentState.step(getData());
        }
    }
}
