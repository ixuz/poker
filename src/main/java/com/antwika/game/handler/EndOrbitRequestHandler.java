package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EndOrbitRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(EndOrbitRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof EndOrbitRequest endOrbitRequest)) return false;

        final GameData.GameStage gameStage = endOrbitRequest.getGameData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        final EndOrbitRequest endOrbitRequest = (EndOrbitRequest) event;
        final GameData gameData = endOrbitRequest.getGameData();

        GameDataUtil.collect(gameData);

        if (gameData.getGameStage() == GameData.GameStage.PREFLOP) {
            gameData.setGameStage(GameData.GameStage.FLOP);
            additionalEvents.add(new BeginOrbitRequest(gameData, 3));
        } else if (gameData.getGameStage() == GameData.GameStage.FLOP) {
            gameData.setGameStage(GameData.GameStage.TURN);
            additionalEvents.add(new BeginOrbitRequest(gameData, 1));
        } else if (gameData.getGameStage() == GameData.GameStage.TURN) {
            gameData.setGameStage(GameData.GameStage.RIVER);
            additionalEvents.add(new BeginOrbitRequest(gameData, 1));
        } else if (gameData.getGameStage() == GameData.GameStage.RIVER) {
            gameData.setGameStage(GameData.GameStage.SHOWDOWN);
            additionalEvents.add(new ShowdownRequest(gameData));
        }

        return additionalEvents;
    }
}
