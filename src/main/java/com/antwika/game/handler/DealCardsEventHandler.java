package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.BeginOrbitRequest;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.IEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DealCardsEventHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(DealCardsEventHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof DealCardsEvent dealCardsEvent)) return false;
        final GameData gameData = dealCardsEvent.getGameData();

        final GameData.GameStage gameStage = gameData.getGameStage();

        return gameStage == GameData.GameStage.HAND_BEGUN;
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final DealCardsEvent dealCardsEvent = (DealCardsEvent) event;
            final GameData gameData = dealCardsEvent.getGameData();

            try {
                for (SeatData seat : gameData.getSeats()) {
                    if (seat.getPlayer() == null) continue;

                    final long cards = seat.getCards();

                    if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

                    logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerData().getPlayerName(), GameDataUtil.toNotation(seat.getCards()));
                }
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }

            gameData.setGameStage(GameData.GameStage.PREFLOP);
            return List.of(new BeginOrbitRequest(gameData, 0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
