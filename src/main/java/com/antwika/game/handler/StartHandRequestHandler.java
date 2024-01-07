package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerJoinRequestEvent;
import com.antwika.game.event.StartHandRequestEvent;
import com.antwika.game.player.Player;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartHandRequestHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(StartHandRequestHandler.class);
    public boolean canHandle(IEvent event) {
        return event instanceof StartHandRequestEvent;
    }

    public void handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final StartHandRequestEvent startHandRequestEvent = (StartHandRequestEvent) event;

        final GameData gameData = startHandRequestEvent.getGameData();

        GameDataUtil.startGame(gameData);

    }
}
