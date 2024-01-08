package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlayerLeaveHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerLeaveHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerLeaveEvent;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerLeaveEvent playerLeaveEvent = (PlayerLeaveEvent) event;
        logger.info("{}: left the game from seat #{}",
                playerLeaveEvent.getPlayer().getPlayerData().getPlayerName(),
                playerLeaveEvent.getSeatData().getSeatIndex() + 1);

        return null;
    }
}
