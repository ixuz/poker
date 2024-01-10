package com.antwika.table.handler.player;

import com.antwika.table.event.IEvent;
import com.antwika.table.event.player.PlayerLeaveRequest;
import com.antwika.table.handler.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlayerLeaveRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerLeaveRequestHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerLeaveRequest;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerLeaveRequest playerLeaveRequest = (PlayerLeaveRequest) event;
        logger.info("{}: left the game from seat #{}",
                playerLeaveRequest.getPlayer().getPlayerData().getPlayerName(),
                playerLeaveRequest.getSeatData().getSeatIndex() + 1);

        return null;
    }
}
