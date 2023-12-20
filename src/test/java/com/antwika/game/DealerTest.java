package com.antwika.game;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.Player;
import com.antwika.game.core.IActor;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.*;
import com.antwika.game.exception.GameException;
import com.antwika.game.util.DealerUtil;
import com.antwika.game.util.TableDataUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DealerTest {
    @Test
    public void construct() {
        new Dealer();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Actor(actorName=Dealer)", new Dealer().toString());
    }

    /* @Test
    @Tag("IntegrationTest")
    public void run() {
        final ITableData table = TableDataUtil.createTableData(4, 10, 20, 0, 1);
        final Dealer dealer = new Dealer();

        final Thread gameThread = new Thread(dealer) {
            @Override
            public void interrupt() {
                dealer.interrupt();
                super.interrupt();
            }
        };

        gameThread.start();

        final IActor alice = new Player("Alice");
        final IActor bob = new Player("Bob");
        final IActor charlie = new Player("Charlie");

        // Alice joins game
        dealer.offerEvent(JoinEvent.builder().actor(alice).tableData(table).seatIndex(0).build());
        dealer.offerEvent(AddChipsEvent.builder().actor(alice).seatIndex(0).amount(1000).build());

        // Bob joins game
        dealer.offerEvent(JoinEvent.builder().actor(bob).tableData(table).seatIndex(1).build());
        dealer.offerEvent(AddChipsEvent.builder().actor(bob).seatIndex(1).amount(2000).build());

        // Charlie joins game
        dealer.offerEvent(JoinEvent.builder().actor(charlie).tableData(table).seatIndex(2).build());
        dealer.offerEvent(AddChipsEvent.builder().actor(charlie).seatIndex(2).amount(3000).build());

        // TODO: Dealer asks Alice: Do you want to play? -> Yes!
        // TODO: Dealer asks Bob: Do you want to play? -> Yes!
        // TODO: Dealer asks Charlie: Do you want to play? -> Yes!
        // TODO: Dealer draws button position. -> Button at Alice
        // TODO: Dealer asks Bob: Do you want to post small blind? -> Yes!
        // TODO: Dealer asks Charlie: Do you want to post big blind? -> Yes!
        // TODO: Dealer deals hole cards...

        dealer.offerEvent(LeaveEvent.builder().actor(alice).build());
        dealer.offerEvent(LeaveEvent.builder().actor(bob).build());
        dealer.offerEvent(LeaveEvent.builder().actor(charlie).build());

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        gameThread.interrupt();
    } */

    /*@Test
    public void join() throws GameException {
        // Arrange
        try (MockedStatic<DealerUtil> staticGameUtil = mockStatic(DealerUtil.class)) {
            final IActor actor = mock(IActor.class);
            final Dealer dealer = new Dealer(null);

            // Act & Assert
            dealer.join(actor, 0);
            staticGameUtil.verify(() -> DealerUtil.join(dealer, 0, actor), times(1));
        }
    }

    @Test
    public void join_whenGameUtilJoinThrows_bubbleException() {
        // Arrange
        try (MockedStatic<DealerUtil> staticGameUtil = mockStatic(DealerUtil.class)) {
            final IActor actor = mock(IActor.class);
            final Dealer dealer = new Dealer(null);

            staticGameUtil.when(() -> DealerUtil.join(dealer, 0, actor)).thenThrow(new GameException());

            // Act & Assert
            assertThrows(GameException.class, () -> dealer.join(actor, 0));
            staticGameUtil.verify(() -> DealerUtil.join(dealer, 0, actor), times(1));
        }
    }

    @Test
    public void leave() throws GameException {
        // Arrange
        try (MockedStatic<DealerUtil> staticGameUtil = mockStatic(DealerUtil.class)) {
            final IActor actor = mock(IActor.class);
            final Dealer dealer = new Dealer(null);

            // Act & Assert
            dealer.leave(actor);
            staticGameUtil.verify(() -> DealerUtil.leave(dealer, actor), times(1));
        }
    }

    @Test
    public void leave_whenGameUtilJoinThrows_bubbleException() throws GameException {
        // Arrange
        try (MockedStatic<DealerUtil> staticGameUtil = mockStatic(DealerUtil.class)) {
            final IActor actor = mock(IActor.class);
            final Dealer dealer = new Dealer(null);

            staticGameUtil.when(() -> DealerUtil.leave(dealer, actor)).thenThrow(new GameException());

            // Act & Assert
            assertThrows(GameException.class, () -> dealer.leave(actor));
            staticGameUtil.verify(() -> DealerUtil.leave(dealer, actor), times(1));
        }
    } */
}
