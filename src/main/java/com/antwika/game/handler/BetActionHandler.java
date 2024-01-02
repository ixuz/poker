package com.antwika.game.handler;

import com.antwika.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetActionHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BetActionHandler.class);

    public boolean canHandle(Event event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.BET);
        }
        return false;
    }

    public void handle(Event event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final Game game = action.game;
        final Seat seat = GameUtil.getSeat(game, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not bet a greater amount than his remaining stack!");
        }
        if (action.amount == 0) {
            throw new RuntimeException("Player can not bet a zero amount!");
        }

        GameUtil.commit(seat, action.amount);
        game.getGameData().setTotalBet(seat.getCommitted());
        game.getGameData().setLastRaise(action.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerName(), action.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);
    }
}
