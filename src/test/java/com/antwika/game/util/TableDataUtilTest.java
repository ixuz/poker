package com.antwika.game.util;

import com.antwika.game.core.IActor;
import com.antwika.test.MockIterator;
import com.antwika.game.core.ISeat;
import com.antwika.game.core.ITableData;
import com.antwika.game.exception.TableException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TableDataUtilTest {
    @Test
    @Tag("UnitTest")
    public void isSeatAvailable_whenSeatIsUnoccupied_returnTrue() {
        // Arrange
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        when(seats.get(0)).thenReturn(seat);
        when(seat.getActor()).thenReturn(null);

        // Act & Assert
        Assertions.assertTrue(TableDataUtil.isSeatAvailable(table, 0));
    }

    @Test
    @Tag("UnitTest")
    public void isSeatAvailable_whenSeatIsOccupied_returnFalse() {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        when(seats.get(0)).thenReturn(seat);
        when(seat.getActor()).thenReturn(actor);

        // Act & Assert
        assertFalse(TableDataUtil.isSeatAvailable(table, 0));
    }

    @Test
    @Tag("UnitTest")
    public void isPlayerSeated_whenPlayerIsSeated_returnTrue() {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seat.getActor()).thenReturn(actor);

        // Act & Assert
        assertTrue(TableDataUtil.isPlayerSeated(table, actor));
    }

    @Test
    @Tag("UnitTest")
    public void isPlayerSeated_whenPlayerIsNotSeated_returnFalse() {
        // Arrange
        final IActor actor = mock(IActor.class);
        final IActor otherActor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seat.getActor()).thenReturn(otherActor);

        // Act & Assert
        assertFalse(TableDataUtil.isPlayerSeated(table, actor));
    }

    @Test
    @Tag("UnitTest")
    public void seatPlayer() throws TableException {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seats.get(0)).thenReturn(seat);
        when(seat.getActor()).thenReturn(null);

        // Act
        TableDataUtil.seatPlayer(table, actor, 0);

        // Assert
        verify(seat, times(2)).getActor();
        verify(seat, times(1)).setActor(actor);
    }

    @Test
    @Tag("UnitTest")
    public void seatPlayer_whenPlayerAlreadySeated_throwTableException() {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seats.get(0)).thenReturn(seat);
        when(seat.getActor()).thenReturn(actor);

        // Act
        assertThrows(TableException.class, () -> TableDataUtil.seatPlayer(table, actor, 0));

        // Assert
        verify(seat, times(1)).getActor();
        verify(seat, times(0)).setActor(any());
    }

    @Test
    @Tag("UnitTest")
    public void seatPlayer_whenSeatOccupied_throwTableException() {
        // Arrange
        final IActor actor = mock(IActor.class);
        final IActor otherActor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seats.get(0)).thenReturn(seat);
        when(seat.getActor()).thenReturn(otherActor);

        // Act
        assertThrows(TableException.class, () -> TableDataUtil.seatPlayer(table, actor, 0));

        // Assert
        verify(seat, times(2)).getActor();
        verify(seat, times(0)).setActor(any());
    }

    @Test
    @Tag("UnitTest")
    public void unseatPlayer() throws TableException {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seat.getActor()).thenReturn(actor);

        // Act
        TableDataUtil.unseatPlayer(table, actor);

        // Assert
        verify(seat, times(1)).getActor();
        verify(seat, times(1)).setActor(null);
    }

    @Test
    @Tag("UnitTest")
    public void unseatPlayer_whenPlayerIsNotSeated_throwTableException() {
        // Arrange
        final IActor actor = mock(IActor.class);
        final ITableData table = mock(ITableData.class);
        final List<ISeat> seats = mock(List.class);
        final ISeat seat = mock(ISeat.class);

        when(table.getSeats()).thenReturn(seats);
        MockIterator.mockIterable(seats, seat);
        when(seat.getActor()).thenReturn(null);

        // Act
        assertThrows(TableException.class, () -> TableDataUtil.unseatPlayer(table, actor));

        // Assert
        verify(seat, times(1)).getActor();
        verify(seat, times(0)).setActor(any());
    }
}
