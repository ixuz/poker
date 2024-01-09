package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.EndOrbitRequest;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.OrbitActionRequest;
import com.antwika.game.event.PlayerActionResponse;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class OrbitActionResponseHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrbitActionResponseHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerActionResponse;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse playerActionResponse = (PlayerActionResponse) event;

        List<IEvent> additionalEvents = null;

        if (playerActionResponse.action.equals(PlayerActionResponse.Type.BET)) {
            additionalEvents = handleBetAction(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.CALL)) {
            additionalEvents = handleCall(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.CHECK)) {
            additionalEvents = handleCheck(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.FOLD)) {
            additionalEvents = handleFold(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.RAISE)) {
            additionalEvents = handleRaise(playerActionResponse);
        }

        return additionalEvents;
    }

    private List<IEvent> handleBetAction(PlayerActionResponse playerActionResponse) {
        final GameData gameData = playerActionResponse.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, playerActionResponse.player);
        if (playerActionResponse.amount > seat.getStack()) {
            throw new RuntimeException("Player can not bet a greater amount than his remaining stack!");
        }
        if (playerActionResponse.amount == 0) {
            throw new RuntimeException("Player can not bet a zero amount!");
        }

        GameDataUtil.commit(seat, playerActionResponse.amount);
        gameData.setTotalBet(seat.getCommitted());
        gameData.setLastRaise(playerActionResponse.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerData().getPlayerName(), playerActionResponse.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (GameDataUtil.hasAllPlayersActed(gameData)) {
            return List.of(new EndOrbitRequest(gameData));
        }

        final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, gameData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new EndOrbitRequest(gameData));
        }

        gameData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(gameData));
    }

    public List<IEvent> handleCall(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not call a greater amount than his remaining stack!");
        }

        GameDataUtil.commit(seat, action.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: calls %d", seat.getPlayer().getPlayerData().getPlayerName(), action.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (GameDataUtil.hasAllPlayersActed(gameData)) {
            return List.of(new EndOrbitRequest(gameData));
        }

        final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, gameData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new EndOrbitRequest(gameData));
        }

        gameData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(gameData));
    }

    private List<IEvent> handleCheck(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, action.player);
        seat.setHasActed(true);
        logger.info("{}: checks", seat.getPlayer().getPlayerData().getPlayerName());
        seat.setHasActed(true);

        if (GameDataUtil.hasAllPlayersActed(gameData)) {
            return List.of(new EndOrbitRequest(gameData));
        }

        final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, gameData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new EndOrbitRequest(gameData));
        }

        gameData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(gameData));
    }

    private List<IEvent> handleFold(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, action.player);
        seat.setHasFolded(true);
        logger.info("{}: folds", seat.getPlayer().getPlayerData().getPlayerName());
        seat.setHasActed(true);

        if (GameDataUtil.hasAllPlayersActed(gameData)) {
            return List.of(new EndOrbitRequest(gameData));
        }

        final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, gameData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new EndOrbitRequest(gameData));
        }

        gameData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(gameData));
    }

    private List<IEvent> handleRaise(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final GameData gameData = action.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not raise a greater amount than his remaining stack!");
        }

        final int smallestValidRaise = Math.min(gameData.getTotalBet() + gameData.getBigBlind(), seat.getStack());
        if (action.amount < smallestValidRaise) {
            throw new RuntimeException("Player must at least raise by one full big blind, or raise all-in for less");
        }

        GameDataUtil.commit(seat, action.amount);
        if (seat.getCommitted() > gameData.getTotalBet()) {
            gameData.setTotalBet(seat.getCommitted());
            gameData.setLastRaise(action.amount);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: raises to %d", seat.getPlayer().getPlayerData().getPlayerName(), seat.getCommitted()));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (GameDataUtil.hasAllPlayersActed(gameData)) {
            return List.of(new EndOrbitRequest(gameData));
        }

        final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, gameData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new EndOrbitRequest(gameData));
        }

        gameData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(gameData));
    }
}
