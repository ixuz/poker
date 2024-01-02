package com.antwika.game.handler;

import com.antwika.game.*;
import com.antwika.game.data.Seat;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerActionResponse;
import com.antwika.game.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoldActionHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(FoldActionHandler.class);

    public boolean canHandle(IEvent event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.FOLD);
        }
        return false;
    }

    public void handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final Game game = action.game;
        final Seat seat = GameUtil.getSeat(game, action.player);
        seat.setHasFolded(true);
        logger.info("{}: folds", seat.getPlayer().getPlayerData().getPlayerName());
        seat.setHasActed(true);
    }
}
