package com.antwika.table.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.ShowdownRequest;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowdownRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShowdownRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof ShowdownRequest showdownRequest)) return false;

        final TableData.GameStage gameStage = showdownRequest.getTableData().getGameStage();

        return gameStage.equals(TableData.GameStage.SHOWDOWN);
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final ShowdownRequest showdownRequest = (ShowdownRequest) event;
            final TableData tableData = showdownRequest.getTableData();

            TableUtil.showdown(tableData);

            tableData.setButtonAt(TableUtil.findNextSeatToAct(tableData, tableData.getButtonAt(), 0, false).getSeatIndex());

            tableData.setGameStage(TableData.GameStage.NONE);

            TableUtil.resetAllSeats(tableData);

            logger.info("--- HAND END ---");

            return null;
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }
}
