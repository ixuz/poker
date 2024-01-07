package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
import com.antwika.game.game.Game;
import com.antwika.game.handler.ActionHandler;
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
        final Game game = new Game();
        game.start();

        final Player player1 = new PremiumPlayer(1L, "Alice");
        final Player player2 = new PremiumPlayer(2L, "Bob");
        final Player player3 = new PremiumPlayer(3L, "Charlie");
        final Player player4 = new RandomPlayer(4L, "David");
        final Player player5 = new RandomPlayer(5L, "Eric");
        final Player player6 = new RandomPlayer(6L, "Filipe");

        game.getActionHandler().offer(new PlayerJoinRequestEvent(game.getGameData(), game.getGameData().getSeats().get(0), player1, 1000));
        game.getActionHandler().offer(new PlayerJoinRequestEvent(game.getGameData(), game.getGameData().getSeats().get(1), player2, 1000));
        game.getActionHandler().offer(new PlayerJoinRequestEvent(game.getGameData(), game.getGameData().getSeats().get(2), player3, 1000));
        game.getActionHandler().offer(new PlayerJoinRequestEvent(game.getGameData(), game.getGameData().getSeats().get(3), player4, 1000));
        game.getActionHandler().offer(new PlayerJoinRequestEvent(game.getGameData(), game.getGameData().getSeats().get(4), player5, 1000));
        game.getActionHandler().offer(new PlayerJoinRequestEvent(game.getGameData(), game.getGameData().getSeats().get(5), player6, 1000));
        game.getActionHandler().offer(new StartHandRequestEvent(game.getGameData()));

        Thread.sleep(5000L);

        game.stopThread();
    }
}
