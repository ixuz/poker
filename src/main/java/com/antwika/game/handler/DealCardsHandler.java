package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.event.BettingRoundEvent;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.IEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealCardsHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DealCardsHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof DealCardsEvent;
    }

    public void handle(IEvent event) {
        try {
            final DealCardsEvent dealCardsEvent = (DealCardsEvent) event;
            final GameData gameData = dealCardsEvent.getGameData();

            GameDataUtil.dealCards(gameData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
