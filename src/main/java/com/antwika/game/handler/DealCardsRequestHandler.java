package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.game.data.DeckData;
import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.BeginBettingRoundRequest;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.DealCardsRequest;
import com.antwika.game.event.IEvent;
import com.antwika.game.log.GameLog;
import com.antwika.game.util.DeckUtil;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DealCardsRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(DealCardsRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof DealCardsRequest dealCardsRequest)) return false;

        final GameData.GameStage gameStage = dealCardsRequest.getGameData().getGameStage();

        return gameStage == GameData.GameStage.HAND_BEGUN;
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final DealCardsRequest dealCardsRequest = (DealCardsRequest) event;
            final GameData gameData = dealCardsRequest.getGameData();

            GameLog.printGameInfo(gameData);
            DeckUtil.resetAndShuffle(gameData.getDeckData());
            GameLog.printTableInfo(gameData);
            GameLog.printTableSeatsInfo(gameData);
            GameDataUtil.forcePostBlinds(gameData, List.of(gameData.getSmallBlind(), gameData.getBigBlind()));

            final List<SeatData> seats = gameData.getSeats();
            final DeckData deckData = gameData.getDeckData();
            logger.info("*** HOLE CARDS ***");
            for (int i = 0; i < seats.size() * 2; i += 1) {
                final int seatIndex = (gameData.getActionAt() + i + 1) % seats.size();
                final SeatData seat = seats.get(seatIndex);
                if (seat.getPlayer() == null) continue;
                Long card = DeckUtil.draw(deckData);
                if (card == null) throw new RuntimeException("The card drawn from the deck is null");
                seat.setCards(seat.getCards() | card);
                try {
                    logger.debug("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
                } catch (NotationException e) {
                    throw new RuntimeException(e);
                }
            }

            GameLog.printTableSeatCardsInfo(gameData);

            return List.of(new DealCardsEvent(gameData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
