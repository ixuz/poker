package com.antwika.game.handler;

import com.antwika.game.*;
import com.antwika.game.data.GameData;
import com.antwika.game.data.Seat;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerActionResponse;
import com.antwika.game.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckActionHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CheckActionHandler.class);

    public boolean canHandle(IEvent event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.CHECK);
        }
        return false;
    }

    public void handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final Seat seat = GameUtil.getSeat(gameData, action.player);
        seat.setHasActed(true);
        logger.info("{}: checks", seat.getPlayer().getPlayerData().getPlayerName());
        seat.setHasActed(true);
    }
}
