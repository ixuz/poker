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

public class BeginBettingRoundRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(BeginBettingRoundRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof BeginBettingRoundRequest beginBettingRoundRequest)) return false;

        final GameData.GameStage gameStage = beginBettingRoundRequest.getGameData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        final BeginBettingRoundRequest beginBettingRoundRequest = (BeginBettingRoundRequest) event;
        final GameData gameData = beginBettingRoundRequest.getGameData();

        try {
            if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
                final int dealCommunityCardCount = beginBettingRoundRequest.getDealCommunityCardCount();
                if (dealCommunityCardCount > 0) {
                    GameDataUtil.dealCommunityCards(gameData, dealCommunityCardCount);
                }

                GameDataUtil.prepareBettingRound(gameData);
                additionalEvents.add(new BettingRoundPlayerActionRequest(gameData));
                // additionalEvents.add(new EndBettingRoundRequest(gameData)); // TODO: Remove this, because we should not collect immediately, we should let players act

                /* while (true) {
                    if (GameDataUtil.countPlayersRemainingInHand(gameData) == 1) {
                        logger.debug("All but one player has folded, hand must end");
                        break; // additionalEvents.add(new EndBettingRoundRequest(gameData));
                    }

                    if (GameDataUtil.getNumberOfPlayersLeftToAct(gameData) < 1) {
                        break; // additionalEvents.add(new EndBettingRoundRequest(gameData));
                    }

                    final List<SeatData> seats = gameData.getSeats();
                    final int actionAt = gameData.getActionAt();

                    final SeatData seat = seats.get(actionAt);

                    final SeatData seatAfter = GameDataUtil.findNextSeatToAct(gameData, actionAt, 0, true);
                    if (seat == null) break;

                    if (seat.isHasFolded()) {
                        seat.setHasActed(true);
                        gameData.setActionAt(seatAfter.getSeatIndex());
                        continue;
                    }

                    final Player player = seat.getPlayer();

                    final int totalBet = gameData.getTotalBet();
                    final int bigBlind = gameData.getBigBlind();
                    final int lastRaise = gameData.getLastRaise();
                    final int toCall = Math.min(seat.getStack(), totalBet - seat.getCommitted());
                    final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, bigBlind));
                    final int minBet = totalBet + minRaise - seat.getCommitted();
                    final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

                    if (toCall > 0) {
                        logger.debug("{}, {} to call", seat.getPlayer(), toCall);
                    } else {
                        logger.debug("{}, Check or bet?", seat.getPlayer());
                    }

                    if (seat.getStack() == 0) {
                        break; // additionalEvents.add(new EndBettingRoundRequest(gameData));
                    }

                    final IEvent response = Player.handleEvent(new PlayerActionRequest(player, gameData, totalBet, toCall, minBet, smallestValidRaise));

                    if (response != null) {
                        additionalEvents.add(response);
                    }

                    if (GameDataUtil.hasAllPlayersActed(gameData)) {
                        break; // additionalEvents.add(new EndBettingRoundRequest(gameData));
                    }

                    final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, actionAt, 0, true);
                    if (theNextSeat == null) {
                        break; // additionalEvents.add(new EndBettingRoundRequest(gameData));
                    }

                    gameData.setActionAt(theNextSeat.getSeatIndex());
                } */
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (GameDataUtil.countPlayersRemainingInHand(gameData) == 1) {
            logger.debug("All but one player has folded, hand must end");
            additionalEvents.add(new EndBettingRoundRequest(gameData));
        }
        return additionalEvents;
    }
}
