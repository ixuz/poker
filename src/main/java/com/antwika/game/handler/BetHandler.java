package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.BettingRoundPlayerActionRequest;
import com.antwika.game.event.EndBettingRoundRequest;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerActionResponse;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BetHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(BetHandler.class);

    public boolean canHandle(IEvent event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.BET);
        }
        return false;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not bet a greater amount than his remaining stack!");
        }
        if (action.amount == 0) {
            throw new RuntimeException("Player can not bet a zero amount!");
        }

        GameDataUtil.commit(seat, action.amount);
        gameData.setTotalBet(seat.getCommitted());
        gameData.setLastRaise(action.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerData().getPlayerName(), action.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (GameDataUtil.hasAllPlayersActed(gameData)) {
            return List.of(new EndBettingRoundRequest(gameData));
        }

        final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, gameData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new EndBettingRoundRequest(gameData));
        }

        gameData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new BettingRoundPlayerActionRequest(gameData));
    }
}
