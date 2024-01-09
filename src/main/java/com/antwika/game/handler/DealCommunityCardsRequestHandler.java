package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.game.data.DeckData;
import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.BeginBettingRoundRequest;
import com.antwika.game.event.DealCardsEvent;
import com.antwika.game.event.DealCommunityCardsRequest;
import com.antwika.game.event.IEvent;
import com.antwika.game.log.GameLog;
import com.antwika.game.util.DeckUtil;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DealCommunityCardsRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(DealCommunityCardsRequestHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof DealCommunityCardsRequest;
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final DealCommunityCardsRequest dealCommunityCardsRequest = (DealCommunityCardsRequest) event;
            final GameData gameData = dealCommunityCardsRequest.getGameData();

            GameDataUtil.dealCommunityCards(gameData, dealCommunityCardsRequest.getCount());

            return List.of(new BeginBettingRoundRequest(gameData, 0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
