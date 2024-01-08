package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.HandBeginEvent;
import com.antwika.game.event.HandBeginRequest;
import com.antwika.game.event.IEvent;
import com.antwika.game.log.GameLog;
import com.antwika.game.util.DeckUtil;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HandBeginRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandBeginRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof HandBeginRequest handBeginRequest)) return false;

        final GameData.GameStage gameStage = handBeginRequest.getGameData().getGameStage();

        return switch (gameStage) {
            case NONE -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final List<IEvent> additionalEvents = new ArrayList<>();

            final HandBeginRequest handBeginRequest = (HandBeginRequest) event;
            final GameData gameData = handBeginRequest.getGameData();

            GameDataUtil.prepareHand(gameData);

            additionalEvents.addAll(GameDataUtil.unseat(gameData, GameDataUtil.findAllBustedSeats(gameData)));

            GameDataUtil.resetAllSeats(gameData);

            gameData.setGameStage(GameData.GameStage.HAND_BEGUN);

            return List.of(
                    new HandBeginEvent(gameData),
                    new DealCardsEvent(gameData)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
