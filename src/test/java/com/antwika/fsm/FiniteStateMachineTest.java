package com.antwika.fsm;

import com.antwika.fsm.state.*;
import com.antwika.fsm.transition.*;
import com.antwika.table.TableDataFactory;
import com.antwika.table.data.TableData;
import com.antwika.table.player.Player;
import com.antwika.table.player.RandomPlayer;
import com.antwika.table.util.TableUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FiniteStateMachineTest {
    @Test
    @Disabled
    public void test() throws InterruptedException {
        final TableData tableData = TableDataFactory.createTableData(1L, "FSM", 5, 1, 2);

        final FiniteStateMachine fsm = new FiniteStateMachine(tableData, 512);

        final FSMState startState = fsm.getStartState();
        final FSMState endState = fsm.getEndState();

        final FSMState handStartedState = new SimpleState("HandStarted");
        final FSMState orbitOngoingState = new SimpleState("OrbitOngoingState");
        final FSMState orbitEndedState = new SimpleState("OrbitEndedState");
        final FSMState winningsDeliveredState = new WinningsDeliveredState();
        final FSMState awaitPlayerActionState = new AwaitPlayerActionState();
        final FSMState handEndedState = new HandEndedState();

        fsm.addState(handStartedState);
        fsm.addState(orbitOngoingState);
        fsm.addState(awaitPlayerActionState);
        fsm.addState(orbitEndedState);
        fsm.addState(winningsDeliveredState);
        fsm.addState(handEndedState);

        fsm.addTransition(new StartHandTransition(startState, handStartedState));
        fsm.addTransition(new CollectChipsTransition(orbitOngoingState, orbitOngoingState));
        fsm.addTransition(new CollectChipsTransition(awaitPlayerActionState, orbitEndedState));
        fsm.addTransition(new DealCommunityCardsTransition(orbitEndedState, orbitEndedState));
        fsm.addTransition(new ShowdownTransition(orbitEndedState, winningsDeliveredState));
        fsm.addTransition(new StartOrbitTransition(handStartedState, orbitOngoingState));
        fsm.addTransition(new CanAnyPlayerActTransition(orbitOngoingState, awaitPlayerActionState));
        fsm.addTransition(new HasPlayerActedTransition(awaitPlayerActionState, orbitOngoingState));
        fsm.addTransition(new EndOrbitTransition(orbitOngoingState, orbitEndedState));
        fsm.addTransition(new CanDeliverWinningsTransition(orbitEndedState, winningsDeliveredState));
        fsm.addTransition(new StartOrbitTransition(orbitEndedState, orbitOngoingState));
        fsm.addTransition(new InstantTransition(winningsDeliveredState, handEndedState));
        fsm.addTransition(new StartHandTransition(handEndedState, handStartedState));

        fsm.start();

        Thread.sleep(1000L);

        final Player alice = new RandomPlayer(1L, "Alice");
        final Player bob = new RandomPlayer(1L, "Bob");
        final Player charlie = new RandomPlayer(1L, "Charlie");
        final Player david = new RandomPlayer(1L, "David");
        final Player eric = new RandomPlayer(1L, "Eric");

        TableUtil.seat(tableData, alice, 0, 1000);
        TableUtil.seat(tableData, bob, 1, 1000);
        TableUtil.seat(tableData, charlie, 2, 1000);
        TableUtil.seat(tableData, david, 3, 1000);
        TableUtil.seat(tableData, eric, 4, 1000);

        fsm.join();
    }
}
