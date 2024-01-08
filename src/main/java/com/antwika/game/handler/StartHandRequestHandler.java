package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.StartHandRequestEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StartHandRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(StartHandRequestHandler.class);
    public boolean canHandle(IEvent event) {
        return event instanceof StartHandRequestEvent;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final StartHandRequestEvent startHandRequestEvent = (StartHandRequestEvent) event;

        final GameData gameData = startHandRequestEvent.getGameData();

        GameDataUtil.startGame(gameData);

        return null;
    }
}
