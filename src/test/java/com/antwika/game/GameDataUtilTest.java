package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
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
        final GameData gameData = GameDataFactory.createGameData(1L, "Lacuna I", 6, 5, 10);

        final Player player1 = new PremiumPlayer(1L, "Alice");
        final Player player2 = new PremiumPlayer(2L, "Bob");
        final Player player3 = new PremiumPlayer(3L, "Charlie");
        final Player player4 = new RandomPlayer(4L, "David");
        final Player player5 = new RandomPlayer(5L, "Eric");
        final Player player6 = new RandomPlayer(6L, "Filipe");

        final ActionHandler actionHandler = new ActionHandler();
        actionHandler.start();

        actionHandler.offer(new PlayerJoinRequestEvent(gameData, gameData.getSeats().get(0), player1, 1000));
        actionHandler.offer(new PlayerJoinRequestEvent(gameData, gameData.getSeats().get(1), player2, 1000));
        actionHandler.offer(new PlayerJoinRequestEvent(gameData, gameData.getSeats().get(2), player3, 1000));
        actionHandler.offer(new PlayerJoinRequestEvent(gameData, gameData.getSeats().get(3), player4, 1000));
        actionHandler.offer(new PlayerJoinRequestEvent(gameData, gameData.getSeats().get(4), player5, 1000));
        actionHandler.offer(new PlayerJoinRequestEvent(gameData, gameData.getSeats().get(5), player6, 1000));

        Thread.sleep(1000L);

        actionHandler.offer(new StartHandRequestEvent(gameData));

        Thread.sleep(1000L);

        while (GameDataUtil.canStartHand(gameData)) {
            if (gameData.getGameStage().equals(GameData.GameStage.NONE)) {
                ActionHandler.handleEvent(new HandBeginEvent(gameData));
                ActionHandler.handleEvent(new DealCardsEvent(gameData));
                gameData.setGameStage(GameData.GameStage.PREFLOP);
            }
            if (gameData.getGameStage().equals(GameData.GameStage.PREFLOP)) {
                ActionHandler.handleEvent(new BettingRoundEvent(gameData, 0));
                gameData.setGameStage(GameData.GameStage.FLOP);
            }
            if (gameData.getGameStage().equals(GameData.GameStage.FLOP)) {
                ActionHandler.handleEvent(new BettingRoundEvent(gameData, 3));
                gameData.setGameStage(GameData.GameStage.TURN);
            }
            if (gameData.getGameStage().equals(GameData.GameStage.TURN)) {
                ActionHandler.handleEvent(new BettingRoundEvent(gameData, 1));
                gameData.setGameStage(GameData.GameStage.RIVER);
            }
            if (gameData.getGameStage().equals(GameData.GameStage.RIVER)) {
                ActionHandler.handleEvent(new BettingRoundEvent(gameData, 1));
                gameData.setGameStage(GameData.GameStage.SHOWDOWN);
            }
            if (gameData.getGameStage().equals(GameData.GameStage.SHOWDOWN)) {
                ActionHandler.handleEvent(new ShowdownEvent(gameData));
            }

            Thread.sleep(200L);
        }

        GameDataUtil.stopGame(gameData);
    }
}
