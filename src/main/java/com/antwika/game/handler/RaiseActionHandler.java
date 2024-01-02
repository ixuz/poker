package com.antwika.game.handler;

import com.antwika.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaiseActionHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RaiseActionHandler.class);

    public boolean canHandle(Event event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.RAISE);
        }
        return false;
    }

    public void handle(Event event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final Game game = action.game;
        final GameData gameData = game.getGameData();
        final Seat seat = GameUtil.getSeat(game, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not raise a greater amount than his remaining stack!");
        }

        final int smallestValidRaise = Math.min(gameData.getTotalBet() + gameData.getBigBlind(), seat.getStack());
        if (action.amount < smallestValidRaise) {
            throw new RuntimeException("Player must at least raise by one full big blind, or raise all-in for less");
        }

        GameUtil.commit(seat, action.amount);
        if (seat.getCommitted() > gameData.getTotalBet()) {
            gameData.setTotalBet(seat.getCommitted());
            gameData.setLastRaise(action.amount);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: raises to %d", seat.getPlayer().getPlayerName(), seat.getCommitted()));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);
    }
}
