package com.antwika.game;

import com.antwika.game.data.GameData;
import com.antwika.game.data.Seat;
import com.antwika.game.util.GameUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlayerTest {
    @Test
    public void calcBetSize() {
        // Arrange
        try (MockedStatic<GameUtil> mockGameUtil = mockStatic(GameUtil.class)) {
            final Game game = mock(Game.class);
            final GameData gameData = mock(GameData.class);
            final Player player = mock(Player.class);
            final Seat seat = mock(Seat.class);
            when(GameUtil.getSeat(game, player)).thenReturn(seat);
            when(game.getGameData()).thenReturn(gameData);
            when(gameData.getLastRaise()).thenReturn(10);
            mockGameUtil.when(() -> GameUtil.countTotalPotAndCommitted(game)).thenReturn(30);
            when(seat.getStack()).thenReturn(1000);

            // Act
            int bet = Player.calcBetSize(game, player, 1.0f);

            // Assert
            assertEquals(50, bet);
        }
    }
    @Test
    public void calcBetSize_when50Percent() {
        // Arrange
        try (MockedStatic<GameUtil> mockGameUtil = mockStatic(GameUtil.class)) {
            final Game game = mock(Game.class);
            final GameData gameData = mock(GameData.class);
            final Player player = mock(Player.class);
            final Seat seat = mock(Seat.class);

            when(GameUtil.getSeat(game, player)).thenReturn(seat);
            when(game.getGameData()).thenReturn(gameData);
            when(gameData.getLastRaise()).thenReturn(10);
            mockGameUtil.when(() -> GameUtil.countTotalPotAndCommitted(game)).thenReturn(30);
            when(seat.getStack()).thenReturn(1000);

            // Act
            int bet = Player.calcBetSize(game, player, 0.5f);

            // Assert
            assertEquals(25, bet);
        }
    }
}
