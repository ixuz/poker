package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.player.Player;
import com.antwika.game.player.PremiumPlayer;
import com.antwika.game.player.RandomPlayer;
import com.antwika.game.util.GameDataUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GameDataUtilTest {
    @Test
    @Disabled
    public void test() throws NotationException, InterruptedException {
        final Player player1 = new PremiumPlayer(1L, "Alice");
        final Player player2 = new PremiumPlayer(2L, "Bob");
        final Player player3 = new PremiumPlayer(3L, "Charlie");
        final Player player4 = new RandomPlayer(4L, "David");
        final Player player5 = new RandomPlayer(5L, "Eric");
        final Player player6 = new RandomPlayer(6L, "Filipe");

        final GameData gameData = GameDataFactory.createGameData(1L, "Lacuna I", 6, 5, 10);

        GameDataUtil.seat(gameData, player1, 0, 1000);
        GameDataUtil.seat(gameData, player2, 1, 1000);
        GameDataUtil.seat(gameData, player3, 2, 1000);
        GameDataUtil.seat(gameData, player4, 3, 1000);
        GameDataUtil.seat(gameData, player5, 4, 1000);
        GameDataUtil.seat(gameData, player6, 5, 1000);

        GameDataUtil.startGame(gameData);

        while (GameDataUtil.canStartHand(gameData)) {
            GameDataUtil.hand(gameData);
            Thread.sleep(200L);
        }

        GameDataUtil.stopGame(gameData);
    }
}
