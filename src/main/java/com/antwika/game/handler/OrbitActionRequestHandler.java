package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.*;
import com.antwika.game.player.Player;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrbitActionRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrbitActionRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof OrbitActionRequest orbitActionRequest)) return false;

        final GameData.GameStage gameStage = orbitActionRequest.getGameData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        try {
            final OrbitActionRequest orbitActionRequest = (OrbitActionRequest) event;
            final GameData gameData = orbitActionRequest.getGameData();

            if (GameDataUtil.countPlayersRemainingInHand(gameData) == 1) {
                logger.debug("All but one player has folded, hand must end");
                return List.of(new OrbitEndRequest(gameData));
            }

            if (GameDataUtil.getNumberOfPlayersLeftToAct(gameData) < 1) {
                return List.of(new OrbitEndRequest(gameData));
            }

            final List<SeatData> seats = gameData.getSeats();
            final int actionAt = gameData.getActionAt();

            final SeatData seat = seats.get(actionAt);

            final SeatData seatAfter = GameDataUtil.findNextSeatToAct(gameData, actionAt, 0, true);
            if (seat == null) {
                return List.of(new OrbitEndRequest(gameData));
            }

            if (seat.isHasFolded()) {
                seat.setHasActed(true);
                gameData.setActionAt(seatAfter.getSeatIndex());
                return List.of(new OrbitActionRequest(gameData));
            }

            final Player player = seat.getPlayer();

            final int totalBet = gameData.getTotalBet();
            final int bigBlind = gameData.getBigBlind();
            final int lastRaise = gameData.getLastRaise();
            final int toCall = Math.min(seat.getStack(), totalBet - seat.getCommitted());
            final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, bigBlind));
            final int minBet = totalBet + minRaise - seat.getCommitted();
            final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

            if (seat.getStack() == 0) {
                return List.of(new OrbitEndRequest(gameData));
            }

            final IEvent response = Player.handleEvent(new PlayerActionRequest(player, gameData, totalBet, toCall, minBet, smallestValidRaise));

            if (response != null) {
                return List.of(response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
