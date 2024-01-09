package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.event.*;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BeginOrbitRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(BeginOrbitRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof BeginOrbitRequest beginOrbitRequest)) return false;

        final GameData.GameStage gameStage = beginOrbitRequest.getGameData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        final BeginOrbitRequest beginOrbitRequest = (BeginOrbitRequest) event;
        final GameData gameData = beginOrbitRequest.getGameData();

        try {
            if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
                GameDataUtil.prepareBettingRound(gameData);

                final int dealCommunityCardCount = beginOrbitRequest.getDealCommunityCardCount();
                if (dealCommunityCardCount > 0) {
                    additionalEvents.add(new DealCommunityCardsRequest(gameData, dealCommunityCardCount));
                } else {
                    additionalEvents.add(new OrbitActionRequest(gameData));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (GameDataUtil.countPlayersRemainingInHand(gameData) == 1) {
            logger.debug("All but one player has folded, hand must end");
            additionalEvents.add(new EndOrbitRequest(gameData));
        }
        return additionalEvents;
    }
}
