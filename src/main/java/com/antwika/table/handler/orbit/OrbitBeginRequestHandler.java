package com.antwika.table.handler.orbit;

import com.antwika.table.data.TableData;
import com.antwika.table.event.*;
import com.antwika.table.event.hand.DealCommunityCardsRequest;
import com.antwika.table.event.orbit.OrbitActionRequest;
import com.antwika.table.event.orbit.OrbitBeginRequest;
import com.antwika.table.event.orbit.OrbitEndRequest;
import com.antwika.table.handler.IHandler;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrbitBeginRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrbitBeginRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof OrbitBeginRequest orbitBeginRequest)) return false;

        final TableData.GameStage gameStage = orbitBeginRequest.getTableData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        final OrbitBeginRequest orbitBeginRequest = (OrbitBeginRequest) event;
        final TableData tableData = orbitBeginRequest.getTableData();

        try {
            if (TableUtil.countPlayersRemainingInHand(tableData) > 1) {
                TableUtil.prepareBettingRound(tableData);

                final int dealCommunityCardCount = orbitBeginRequest.getDealCommunityCardCount();
                if (dealCommunityCardCount > 0) {
                    additionalEvents.add(new DealCommunityCardsRequest(tableData, dealCommunityCardCount));
                } else {
                    additionalEvents.add(new OrbitActionRequest(tableData));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (TableUtil.countPlayersRemainingInHand(tableData) == 1) {
            logger.debug("All but one player has folded, hand must end");
            additionalEvents.add(new OrbitEndRequest(tableData));
        }
        return additionalEvents;
    }
}
