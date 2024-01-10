package com.antwika.table.handler.orbit;

import com.antwika.table.data.TableData;
import com.antwika.table.event.*;
import com.antwika.table.event.hand.ShowdownRequest;
import com.antwika.table.event.orbit.OrbitBeginRequest;
import com.antwika.table.event.orbit.OrbitEndRequest;
import com.antwika.table.handler.IHandler;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class OrbitEndRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrbitEndRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof OrbitEndRequest orbitEndRequest)) return false;

        final TableData.GameStage gameStage = orbitEndRequest.getTableData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        final OrbitEndRequest orbitEndRequest = (OrbitEndRequest) event;
        final TableData tableData = orbitEndRequest.getTableData();

        TableUtil.collect(tableData);

        if (tableData.getGameStage() == TableData.GameStage.PREFLOP) {
            tableData.setGameStage(TableData.GameStage.FLOP);
            additionalEvents.add(new OrbitBeginRequest(tableData, 3));
        } else if (tableData.getGameStage() == TableData.GameStage.FLOP) {
            tableData.setGameStage(TableData.GameStage.TURN);
            additionalEvents.add(new OrbitBeginRequest(tableData, 1));
        } else if (tableData.getGameStage() == TableData.GameStage.TURN) {
            tableData.setGameStage(TableData.GameStage.RIVER);
            additionalEvents.add(new OrbitBeginRequest(tableData, 1));
        } else if (tableData.getGameStage() == TableData.GameStage.RIVER) {
            tableData.setGameStage(TableData.GameStage.SHOWDOWN);
            additionalEvents.add(new ShowdownRequest(tableData));
        }

        return additionalEvents;
    }
}
