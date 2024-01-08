package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlayerJoinHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerJoinHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerJoinEvent;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerJoinEvent playerJoinEvent = (PlayerJoinEvent) event;
        logger.info("{}: joined the game at seat #{}",
                playerJoinEvent.getPlayer().getPlayerData().getPlayerName(),
                playerJoinEvent.getSeatData().getSeatIndex() + 1);

        return null;
    }
}
