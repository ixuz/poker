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
    final ActionHandler actionHandler = new ActionHandler("Game.actionHandler");

    boolean shouldStopAfterHand = false;

    public Game() {
        super("Game");
        gameData = GameDataFactory.createGameData(1L, "Lacuna I", 6, 5, 10);
    }

    @Override
    public synchronized void stopThread() {
        shouldStopAfterHand = true;
    }

    @Override
    public synchronized void start() {
        super.start();
        actionHandler.start();
    }

    @Override
    public void run() {
        setEventHandlerState(EventHandlerState.STARTING);
        setRunning(true);

        setEventHandlerState(EventHandlerState.STARTED);
        while (getEventHandlerState().equals(EventHandlerState.STARTED)) {
            try {
                Thread.sleep(100L);
                if (GameDataUtil.canStartHand(gameData)) {
                    if (shouldStopAfterHand) {
                        super.stopThread();
                        break;
                    }
                    actionHandler.offer(new HandBeginEvent(gameData));
                }
            } catch (InterruptedException e) {
                logger.info("Interrupted", e);
                break;
            }
        }

        setEventHandlerState(EventHandlerState.STOPPING);
        setRunning(false);

        actionHandler.stopThread();
        try {
            actionHandler.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        setEventHandlerState(EventHandlerState.STOPPED);
        logger.info("Game thread ended");
    }
}
