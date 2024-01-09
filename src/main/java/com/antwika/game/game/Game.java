package com.antwika.game.game;

import com.antwika.game.GameDataFactory;
import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
import com.antwika.game.handler.*;
import com.antwika.game.util.GameDataUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
public class Game extends ActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private final GameData gameData;

    private final long maxHandCount;

    boolean shouldStopAfterHand = false;

    public Game(long maxHandCount, long eventPollTimeoutMillis) {
        super(new AggregateHandler(List.of(
                new ShowdownRequestHandler(),
                new HandBeginRequestHandler(),
                new PlayerJoinRequestHandler(),
                new PlayerLeaveRequestHandler(),
                new OrbitBeginRequestHandler(),
                new OrbitEndRequestHandler(),
                new OrbitActionRequestHandler(),
                new OrbitActionResponseHandler(),
                new DealCommunityCardsRequestHandler()
        )), "Game", eventPollTimeoutMillis);
        this.maxHandCount = maxHandCount;
        gameData = GameDataFactory.createGameData(1L, "Lacuna I", 6, 5, 10);
    }

    @Override
    public synchronized void stopThread() {
        shouldStopAfterHand = true;
    }

    @Override
    protected synchronized void noEventHandle() {
        try {
            if (gameData.getGameStage().equals(GameData.GameStage.NONE)) {
                if (GameDataUtil.canStartHand(gameData)) {
                    if (gameData.getHandId() >= maxHandCount) {
                        shouldStopAfterHand = true;
                    }
                    if (shouldStopAfterHand) {
                        super.stopThread();
                        return;
                    }
                    offer(new HandBeginRequest(gameData));
                }
            }
        } catch (InterruptedException e) {
            logger.info("Interrupted", e);
        }
    }
}
