package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.HandBeginEvent;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerLeaveEvent;
import com.antwika.game.log.GameLog;
import com.antwika.game.util.DeckUtil;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HandBeginHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandBeginHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof HandBeginEvent;
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final List<IEvent> additionalEvents = new ArrayList<>();

            final HandBeginEvent handBeginEvent = (HandBeginEvent) event;
            final GameData gameData = handBeginEvent.getGameData();

            GameDataUtil.prepareHand(gameData);

            additionalEvents.addAll(GameDataUtil.unseat(gameData, GameDataUtil.findAllBustedSeats(gameData)));

            GameDataUtil.resetAllSeats(gameData);
            GameLog.printGameInfo(gameData);
            DeckUtil.resetAndShuffle(gameData.getDeckData());
            GameLog.printTableInfo(gameData);
            GameLog.printTableSeatsInfo(gameData);
            GameDataUtil.forcePostBlinds(gameData, List.of(gameData.getSmallBlind(), gameData.getBigBlind()));

            return List.of(new DealCardsEvent(gameData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
