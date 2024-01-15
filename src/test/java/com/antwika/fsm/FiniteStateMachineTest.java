package com.antwika.fsm;

import org.junit.jupiter.api.Test;

public class FiniteStateMachineTest {
    @Test
    public void test() {
        final FiniteStateMachine fsm = new FiniteStateMachine();

        final State startState = fsm.getStartState();
        final State intermediateState = new IntermediateState();
        final State endState = fsm.getEndState();

        fsm.addState(intermediateState);

        final Transition startToIntermediateTransition = new InstantTransition(startState, intermediateState);
        final Transition intermediateToEndTransition = new CounterTransition(intermediateState, endState, 1);

        fsm.addTransition(startToIntermediateTransition);
        fsm.addTransition(intermediateToEndTransition);

        while (!fsm.isEndStageReached()) {
            fsm.step();
        }
    }
}
