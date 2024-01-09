package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.ShowdownRequest;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowdownRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShowdownRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof ShowdownRequest showdownRequest)) return false;

        final GameData.GameStage gameStage = showdownRequest.getGameData().getGameStage();

        return gameStage.equals(GameData.GameStage.SHOWDOWN);
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final ShowdownRequest showdownRequest = (ShowdownRequest) event;
            final GameData gameData = showdownRequest.getGameData();

            GameDataUtil.showdown(gameData);

            gameData.setButtonAt(GameDataUtil.findNextSeatToAct(gameData, gameData.getButtonAt(), 0, false).getSeatIndex());

            gameData.setGameStage(GameData.GameStage.NONE);

            GameDataUtil.resetAllSeats(gameData);

            logger.info("--- HAND END ---");

            return null;
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }
}
