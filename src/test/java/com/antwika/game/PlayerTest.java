package com.antwika.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {
    @Test
    public void calcBetSize() {
        // Arrange
        final Game game = mock(Game.class);
        final Player player = mock(Player.class);
        final Seat seat = mock(Seat.class);

        when(game.getSeat(player)).thenReturn(seat);
        when(game.getLastRaise()).thenReturn(10);
        when(game.getTotalPot(true)).thenReturn(30);
        when(seat.getStack()).thenReturn(1000);

        // Act
        int bet = Player.calcBetSize(game, player, 1.0f);

        // Assert
        assertEquals(50, bet);
    }
    @Test
    public void calcBetSize_when50Percent() {
        // Arrange
        final Game game = mock(Game.class);
        final Player player = mock(Player.class);
        final Seat seat = mock(Seat.class);

        when(game.getSeat(player)).thenReturn(seat);
        when(game.getLastRaise()).thenReturn(10);
        when(game.getTotalPot(true)).thenReturn(30);
        when(seat.getStack()).thenReturn(1000);

        // Act
        int bet = Player.calcBetSize(game, player, 0.5f);

        // Assert
        assertEquals(25, bet);
    }
}
