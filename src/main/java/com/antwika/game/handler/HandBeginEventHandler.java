package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
import com.antwika.game.log.GameLog;
import com.antwika.game.util.DeckUtil;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HandBeginEventHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandBeginEventHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof HandBeginEvent;
    }

    public List<IEvent> handle(IEvent event) {
        final HandBeginEvent handBeginEvent = (HandBeginEvent) event;
        final GameData gameData = handBeginEvent.getGameData();
        logger.info("--- HAND BEGIN ---");
        return List.of(new DealCardsRequest(gameData));
    }
}
