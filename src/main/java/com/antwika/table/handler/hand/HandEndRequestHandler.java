package com.antwika.table.handler.hand;

import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.hand.HandEndRequest;
import com.antwika.table.handler.IHandler;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class HandEndRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandEndRequestHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof HandEndRequest;
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final List<IEvent> additionalEvents = new ArrayList<>();

            final HandEndRequest handEndRequest = (HandEndRequest) event;
            final TableData tableData = handEndRequest.getTableData();

            tableData.setButtonAt(TableUtil.findNextSeatToAct(tableData, tableData.getButtonAt(), 0, false).getSeatIndex());

            tableData.setGameStage(TableData.GameStage.NONE);

            TableUtil.resetAllSeats(tableData);

            logger.info("--- HAND END ---");

            return additionalEvents;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
