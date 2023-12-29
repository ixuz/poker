package com.antwika.game;

import com.antwika.common.exception.NotationException;
import org.junit.jupiter.api.Test;

public class GameTest {
    @Test
    public void test() throws NotationException {
        Player player1 = new Player(1L, "Alice");
        Player player2 = new Player(2L, "Bob");
        Player player3 = new Player(3L, "Charlie");
        Game game = new Game(1L, "Lacuna I", 3, 5, 10);
        game.join(player1);
        game.join(player2);
        game.join(player3);
        game.startGame();
        game.gameLoop();
        game.leave(player1);
        game.leave(player2);
        game.leave(player3);
        game.stopGame();
    }
}
