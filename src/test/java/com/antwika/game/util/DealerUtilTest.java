package com.antwika.game.util;

import com.antwika.game.actor.Dealer;
import com.antwika.game.core.IActor;
import com.antwika.test.MockIterator;
import com.antwika.game.core.ISeat;
import com.antwika.game.core.ITableData;
import com.antwika.game.exception.GameException;
import com.antwika.game.exception.TableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DealerUtilTest {
    @Test
    @Tag("UnitTest")
    public void isSeatAvailable_whenUnoccupied_returnTrue() {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            when(dealer.getTable()).thenReturn(table);
            staticTableUtil.when(() -> TableDataUtil.isSeatAvailable(table, 0)).thenReturn(true);

            // Act & Assert
            Assertions.assertTrue(DealerUtil.isSeatAvailable(dealer, 0));
            staticTableUtil.verify(() -> TableDataUtil.isSeatAvailable(table, 0), times(1));
        }
    }

    @Test
    @Tag("UnitTest")
    public void isSeatAvailable_whenOccupied_returnFalse() {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            when(dealer.getTable()).thenReturn(table);
            staticTableUtil.when(() -> TableDataUtil.isSeatAvailable(table, 0)).thenReturn(false);

            // Act & Assert
            assertFalse(DealerUtil.isSeatAvailable(dealer, 0));
            staticTableUtil.verify(() -> TableDataUtil.isSeatAvailable(table, 0), times(1));
        }
    }

    @Test
    @Tag("UnitTest")
    public void isPlayerSeated_whenNotSeated_returnFalse() {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            final IActor actor = mock(IActor.class);
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            when(dealer.getTable()).thenReturn(table);
            staticTableUtil.when(() -> TableDataUtil.isPlayerSeated(table, actor)).thenReturn(false);

            // Act & Assert
            assertFalse(DealerUtil.isPlayerSeated(dealer, actor));
            staticTableUtil.verify(() -> TableDataUtil.isPlayerSeated(table, actor), times(1));
        }
    }

    @Test
    @Tag("UnitTest")
    public void isPlayerSeated_whenSeated_returnTrue() {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            final IActor actor = mock(IActor.class);
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            when(dealer.getTable()).thenReturn(table);
            staticTableUtil.when(() -> TableDataUtil.isPlayerSeated(table, actor)).thenReturn(true);

            // Act & Assert
            assertTrue(DealerUtil.isPlayerSeated(dealer, actor));
            staticTableUtil.verify(() -> TableDataUtil.isPlayerSeated(table, actor), times(1));
        }
    }

    @Test
    @Tag("UnitTest")
    public void join() throws GameException {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            // Arrange
            final IActor actor = mock(IActor.class);
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            when(dealer.getTable()).thenReturn(table);

            // Act & Assert
            DealerUtil.join(dealer, 0, actor);
            staticTableUtil.verify(() -> TableDataUtil.seatPlayer(table, actor, 0), times(1));
        }
    }

    @Test
    @Tag("UnitTest")
    public void join_whenTableUtilSeatPlayerThrows_rethrowGameException() {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            final IActor actor = mock(IActor.class);
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            staticTableUtil.when(() -> TableDataUtil.seatPlayer(table, actor, 0)).thenThrow(new TableException());

            when(dealer.getTable()).thenReturn(table);

            // Act & Assert
            assertThrows(GameException.class, () -> DealerUtil.join(dealer, 0, actor));
            staticTableUtil.verify(() -> TableDataUtil.seatPlayer(table, actor, 0), times(1));
        }
    }

    @Test
    @Tag("UnitTest")
    public void leave() throws GameException {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);
        final Dealer dealer = mock(Dealer.class);

        when(dealer.getTable()).thenReturn(table);
        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seat.getActor()).thenReturn(actor);

        // Act & Assert
        DealerUtil.leave(dealer, actor);
        verify(seat, times(1)).getActor();
        verify(seat, times(1)).setActor(null);
    }

    @Test
    @Tag("UnitTest")
    public void leave_whenTableUtilUnseatPlayerThrows_rethrowGameException() {
        // Arrange
        try (MockedStatic<TableDataUtil> staticTableUtil = mockStatic(TableDataUtil.class)) {
            final IActor actor = mock(IActor.class);
            final ITableData table = mock(ITableData.class);
            final Dealer dealer = mock(Dealer.class);

            when(dealer.getTable()).thenReturn(table);
            staticTableUtil.when(() -> TableDataUtil.unseatPlayer(table, actor)).thenThrow(new TableException());

            // Act & Assert
            assertThrows(GameException.class, () -> DealerUtil.leave(dealer, actor));
            staticTableUtil.verify(() -> TableDataUtil.unseatPlayer(table, actor), times(1));
        }
    }
}
