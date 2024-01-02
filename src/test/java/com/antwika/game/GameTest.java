package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.player.Player;
import com.antwika.game.util.GameUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GameTest {
    @Test
    @Disabled
    public void test() throws NotationException, InterruptedException {
        final Player player1 = new Player(1L, "Alice");
        final Player player2 = new Player(2L, "Bob");
        final Player player3 = new Player(3L, "Charlie");
        final Player player4 = new Player(4L, "David");
        final Player player5 = new Player(5L, "Eric");
        final Player player6 = new Player(6L, "Filipe");

        final GameData gameData = GameDataFactory.createGameData(1L, "Lacuna I", 6, 5, 10);

        GameUtil.seat(gameData, player1, 0, 1000);
        GameUtil.seat(gameData, player2, 1, 1000);
        GameUtil.seat(gameData, player3, 2, 1000);
        GameUtil.seat(gameData, player4, 3, 1000);
        GameUtil.seat(gameData, player5, 4, 1000);
        GameUtil.seat(gameData, player6, 5, 1000);

        GameUtil.startGame(gameData);

        while (GameUtil.canStartHand(gameData)) {
            GameUtil.hand(gameData);
            Thread.sleep(200L);
        }

        GameUtil.stopGame(gameData);
    }
}
