package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.BettingRoundEvent;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerActionRequest;
import com.antwika.game.player.Player;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BettingRoundHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(BettingRoundHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof BettingRoundEvent bettingRoundEvent)) return false;

        final GameData.GameStage gameStage = bettingRoundEvent.getGameData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        try {
            final BettingRoundEvent bettingRoundEvent = (BettingRoundEvent) event;
            final GameData gameData = bettingRoundEvent.getGameData();

            if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
                final int dealCommunityCardCount = bettingRoundEvent.getDealCommunityCardCount();
                if (dealCommunityCardCount > 0) {
                    GameDataUtil.dealCommunityCards(gameData, dealCommunityCardCount);
                }

                GameDataUtil.prepareBettingRound(gameData);

                while (true) {
                    if (GameDataUtil.countPlayersRemainingInHand(gameData) == 1) {
                        logger.debug("All but one player has folded, hand must end");
                        break;
                    }

                    if (GameDataUtil.getNumberOfPlayersLeftToAct(gameData) < 1) {
                        break;
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
                        break;
                    }

                    final IEvent response = Player.handleEvent(new PlayerActionRequest(player, gameData, totalBet, toCall, minBet, smallestValidRaise));

                    if (response != null) {
                        additionalEvents.add(response);
                    }

                    if (GameDataUtil.hasAllPlayersActed(gameData)) {
                        break;
                    }

                    final SeatData theNextSeat = GameDataUtil.findNextSeatToAct(gameData, actionAt, 0, true);
                    if (theNextSeat == null) {
                        break;
                    }

                    gameData.setActionAt(theNextSeat.getSeatIndex());
                }

                GameDataUtil.collect(gameData);

                logger.debug("Betting round ended");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            final BettingRoundEvent bettingRoundEvent = (BettingRoundEvent) event;
            final GameData gameData = bettingRoundEvent.getGameData();
            if (gameData.getGameStage() == GameData.GameStage.PREFLOP) {
                gameData.setGameStage(GameData.GameStage.FLOP);
            } else if (gameData.getGameStage() == GameData.GameStage.FLOP) {
                gameData.setGameStage(GameData.GameStage.TURN);
            } else if (gameData.getGameStage() == GameData.GameStage.TURN) {
                gameData.setGameStage(GameData.GameStage.RIVER);
            } else if (gameData.getGameStage() == GameData.GameStage.RIVER) {
                gameData.setGameStage(GameData.GameStage.SHOWDOWN);
            }
        }

        return additionalEvents;
    }
}
