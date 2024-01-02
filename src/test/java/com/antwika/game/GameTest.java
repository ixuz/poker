package com.antwika.game;

import com.antwika.common.exception.NotationException;
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

        final Game game = new Game(1L, "Lacuna I", 6, 5, 10);

        GameUtil.seat(game, player1, 0, 1000);
        GameUtil.seat(game, player2, 1, 1000);
        GameUtil.seat(game, player3, 2, 1000);
        GameUtil.seat(game, player4, 3, 1000);
        GameUtil.seat(game, player5, 4, 1000);
        GameUtil.seat(game, player6, 5, 1000);

        GameUtil.startGame(game);

        while (GameUtil.canStartHand(game)) {
            GameUtil.hand(game);
            Thread.sleep(200L);
        }

        GameUtil.stopGame(game);
    }
}
