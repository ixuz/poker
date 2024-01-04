package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.BettingRoundEvent;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.ShowdownEvent;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BettingRoundHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BettingRoundHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof BettingRoundEvent;
    }

    public void handle(IEvent event) {
        try {
            final BettingRoundEvent bettingRoundEvent = (BettingRoundEvent) event;
            final GameData gameData = bettingRoundEvent.getGameData();

            if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
                switch (Long.bitCount(gameData.getCards())) {
                    case 0 -> GameDataUtil.dealCommunityCards(gameData, 3);
                    case 3, 4 -> GameDataUtil.dealCommunityCards(gameData, 1);
                }

                GameDataUtil.bettingRound(gameData);
                GameDataUtil.collect(gameData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
