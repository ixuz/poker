package com.antwika.game.game;

import com.antwika.game.GameDataFactory;
import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
import com.antwika.game.handler.ActionHandler;
import com.antwika.game.util.GameDataUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Game extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private final GameData gameData;
    final ActionHandler actionHandler = new ActionHandler();

    public Game() {
        gameData = GameDataFactory.createGameData(1L, "Lacuna I", 6, 5, 10);
    }

    @Override
    public synchronized void start() {
        actionHandler.start();
        super.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500L);
                if (GameDataUtil.canStartHand(gameData)) {
                    actionHandler.offer(new HandBeginEvent(gameData));
                    actionHandler.offer(new DealCardsEvent(gameData));
                    actionHandler.offer(new BettingRoundEvent(gameData, 0));
                    actionHandler.offer(new BettingRoundEvent(gameData, 3));
                    actionHandler.offer(new BettingRoundEvent(gameData, 1));
                    actionHandler.offer(new BettingRoundEvent(gameData, 1));
                    actionHandler.offer(new ShowdownEvent(gameData));
                }
            } catch (InterruptedException e) {
                logger.info("Interrupted", e);
            }
        }
    }
}
