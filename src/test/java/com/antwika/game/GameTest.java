package com.antwika.game;

import org.junit.jupiter.api.Test;

public class GameTest {
    @Test
    public void test() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        Player player3 = new Player("Charlie");

        Game game = new Game(3);
        game.join(player1);
        game.join(player2);
        game.join(player3);

        game.start();

        game.blinds();

        game.dealCards();

        game.bettingRound();

        game.collect();

        game.dealFlop();
        game.bettingRound();

        game.collect();

        game.dealTurn();
        game.bettingRound();

        game.collect();

        game.dealRiver();
        game.bettingRound();

        game.collect();

        game.showdown();
    }
}
