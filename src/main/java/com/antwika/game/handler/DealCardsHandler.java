package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.game.data.DeckData;
import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.BeginBettingRoundRequest;
import com.antwika.game.event.BettingRoundEvent;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.IEvent;
import com.antwika.game.log.GameLog;
import com.antwika.game.util.DeckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DealCardsHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(DealCardsHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof DealCardsEvent dealCardsEvent)) return false;

        final GameData.GameStage gameStage = dealCardsEvent.getGameData().getGameStage();

        return switch (gameStage) {
            case NONE -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final DealCardsEvent dealCardsEvent = (DealCardsEvent) event;
            final GameData gameData = dealCardsEvent.getGameData();

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
            gameData.setGameStage(GameData.GameStage.PREFLOP);

            return List.of(new BeginBettingRoundRequest(gameData, 0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
