package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.ShowdownEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowdownHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShowdownHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof ShowdownEvent showdownEvent)) return false;

        final GameData.GameStage gameStage = showdownEvent.getGameData().getGameStage();

        return switch (gameStage) {
            case SHOWDOWN -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final ShowdownEvent showdownEvent = (ShowdownEvent) event;
            final GameData gameData = showdownEvent.getGameData();
                GameDataUtil.showdown(gameData);

            gameData.setButtonAt(GameDataUtil.findNextSeatToAct(gameData, gameData.getButtonAt(), 0, false).getSeatIndex());

            gameData.setGameStage(GameData.GameStage.NONE);

            GameDataUtil.resetAllSeats(gameData);
            logger.info("--- HAND END ---");
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
