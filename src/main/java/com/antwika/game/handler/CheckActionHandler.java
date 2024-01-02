package com.antwika.game.handler;

import com.antwika.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckActionHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CheckActionHandler.class);

    public boolean canHandle(Event event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.CHECK);
        }
        return false;
    }

    public void handle(Event event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final Game game = action.game;
        final Seat seat = GameUtil.getSeat(game, action.player);
        seat.setHasActed(true);
        logger.info("{}: checks", seat.getPlayer().getPlayerName());
        seat.setHasActed(true);
    }
}
