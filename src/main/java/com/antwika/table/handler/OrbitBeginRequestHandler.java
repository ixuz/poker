package com.antwika.table.handler;

import com.antwika.table.data.TableData;
import com.antwika.table.event.*;
import com.antwika.table.util.TableDataUtil;
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
            if (TableDataUtil.countPlayersRemainingInHand(tableData) > 1) {
                TableDataUtil.prepareBettingRound(tableData);

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

        if (TableDataUtil.countPlayersRemainingInHand(tableData) == 1) {
            logger.debug("All but one player has folded, hand must end");
            additionalEvents.add(new OrbitEndRequest(tableData));
        }
        return additionalEvents;
    }
}
