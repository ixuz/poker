package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerJoinEvent;
import com.antwika.game.event.ShowdownEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerJoinHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerJoinHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerJoinEvent;
    }

    public void handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerJoinEvent playerJoinEvent = (PlayerJoinEvent) event;
        logger.info("{}: joined the game at seat #{}",
                playerJoinEvent.getPlayer().getPlayerData().getPlayerName(),
                playerJoinEvent.getSeatData().getSeatIndex() + 1);
    }
}
