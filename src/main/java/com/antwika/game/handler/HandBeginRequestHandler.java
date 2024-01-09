package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.game.data.DeckData;
import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.*;
import com.antwika.game.util.DeckUtil;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HandBeginRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandBeginRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof HandBeginRequest handBeginRequest)) return false;

        final GameData.GameStage gameStage = handBeginRequest.getGameData().getGameStage();

        return switch (gameStage) {
            case NONE -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final List<IEvent> additionalEvents = new ArrayList<>();

            final HandBeginRequest handBeginRequest = (HandBeginRequest) event;
            final GameData gameData = handBeginRequest.getGameData();

            GameDataUtil.prepareHand(gameData);

            additionalEvents.addAll(GameDataUtil.unseat(gameData, GameDataUtil.findAllBustedSeats(gameData)));

            GameDataUtil.resetAllSeats(gameData);

            gameData.setGameStage(GameData.GameStage.HAND_BEGUN);

            logger.info("--- HAND BEGIN ---");

            logger.info("Poker Hand #{}: {} ({}/{}) - {}",
                    gameData.getHandId(),
                    gameData.getGameType(),
                    gameData.getSmallBlind(),
                    gameData.getBigBlind(),
                    new Date());

            DeckUtil.resetAndShuffle(gameData.getDeckData());

            logger.info("Table '{}' {}-max Seat #{} is the button",
                    gameData.getTableName(),
                    gameData.getSeats().size(),
                    gameData.getButtonAt() + 1);

            for (SeatData seat : gameData.getSeats()) {
                if (seat.getPlayer() == null) continue;

                logger.info("Seat {}: {} ({} in chips) ",
                        seat.getSeatIndex() + 1,
                        seat.getPlayer().getPlayerData().getPlayerName(),
                        seat.getStack());
            }

            GameDataUtil.forcePostBlinds(gameData, List.of(gameData.getSmallBlind(), gameData.getBigBlind()));

            final List<SeatData> seats = gameData.getSeats();
            final DeckData deckData = gameData.getDeckData();
            logger.info("*** HOLE CARDS ***");
            for (int i = 0; i < seats.size() * 2; i += 1) {
                final int seatIndex = (gameData.getActionAt() + i + 1) % seats.size();
                final SeatData seat = seats.get(seatIndex);
                if (seat.getPlayer() == null) continue;
                final Long card = DeckUtil.draw(deckData);
                if (card == null) throw new RuntimeException("The card drawn from the deck is null");
                seat.setCards(seat.getCards() | card);
                try {
                    logger.debug("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
                } catch (NotationException e) {
                    throw new RuntimeException(e);
                }
            }

            for (SeatData seat : gameData.getSeats()) {
                if (seat.getPlayer() == null) continue;

                final long cards = seat.getCards();

                if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

                logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerData().getPlayerName(), GameDataUtil.toNotation(seat.getCards()));
            }

            gameData.setGameStage(GameData.GameStage.PREFLOP);

            return List.of(new BeginOrbitRequest(gameData, 0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
