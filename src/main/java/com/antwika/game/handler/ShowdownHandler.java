package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.ShowdownEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowdownHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShowdownHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof ShowdownEvent;
    }

    public void handle(IEvent event) {
        try {
            final ShowdownEvent showdownEvent = (ShowdownEvent) event;
            final GameData gameData = showdownEvent.getGameData();
                GameDataUtil.showdown(gameData);

            gameData.setButtonAt(GameDataUtil.findNextSeatToAct(gameData, gameData.getButtonAt(), 0, false).getSeatIndex());

            gameData.setGameStage(GameData.GameStage.NONE);
            logger.info("--- HAND END ---");
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }
}
