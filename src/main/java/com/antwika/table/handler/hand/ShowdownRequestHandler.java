package com.antwika.table.handler.hand;

import com.antwika.common.exception.NotationException;
import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.hand.HandEndRequest;
import com.antwika.table.event.hand.ShowdownRequest;
import com.antwika.table.handler.IHandler;
import com.antwika.table.util.TableUtil;
import java.util.List;

public class ShowdownRequestHandler implements IHandler {
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

            return List.of(new HandEndRequest(tableData));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }
}
