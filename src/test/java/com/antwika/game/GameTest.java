package com.antwika.game;

import com.antwika.common.exception.NotationException;
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

        game.open();

        game.join(player1);
        game.join(player2);
        game.join(player3);
        game.join(player4);
        game.join(player5);
        game.join(player6);

        while (game.canDealNextHand()) {
            game.dealHand();
            Thread.sleep(200L);
        }

        game.close();

        game.leave(player1);
        game.leave(player2);
        game.leave(player3);
        game.leave(player4);
        game.leave(player5);
        game.leave(player6);
    }
}
