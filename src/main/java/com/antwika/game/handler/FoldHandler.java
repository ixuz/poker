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

public class FoldHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(FoldHandler.class);

    public boolean canHandle(IEvent event) {
        if (event instanceof PlayerActionResponse e) {
            return e.action.equals(PlayerActionResponse.Type.FOLD);
        }
        return false;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, action.player);
        seat.setHasFolded(true);
        logger.info("{}: folds", seat.getPlayer().getPlayerData().getPlayerName());
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
