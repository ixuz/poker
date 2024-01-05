package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerJoinEvent;
import com.antwika.game.event.PlayerLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerLeaveHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerLeaveHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerLeaveEvent;
    }

    public void handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerLeaveEvent playerLeaveEvent = (PlayerLeaveEvent) event;
        logger.info("{}: left the game from seat #{}",
                playerLeaveEvent.getPlayer().getPlayerData().getPlayerName(),
                playerLeaveEvent.getSeatData().getSeatIndex() + 1);
    }
}
