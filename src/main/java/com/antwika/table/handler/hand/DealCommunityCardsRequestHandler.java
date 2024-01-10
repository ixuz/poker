package com.antwika.table.handler.hand;

import com.antwika.table.data.TableData;
import com.antwika.table.event.orbit.OrbitBeginRequest;
import com.antwika.table.event.hand.DealCommunityCardsRequest;
import com.antwika.table.event.IEvent;
import com.antwika.table.handler.IHandler;
import com.antwika.table.util.TableUtil;
import java.util.List;

public class DealCommunityCardsRequestHandler implements IHandler {
    public boolean canHandle(IEvent event) {
        return event instanceof DealCommunityCardsRequest;
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final DealCommunityCardsRequest dealCommunityCardsRequest = (DealCommunityCardsRequest) event;
            final TableData tableData = dealCommunityCardsRequest.getTableData();

            TableUtil.dealCommunityCards(tableData, dealCommunityCardsRequest.getCount());

            return List.of(new OrbitBeginRequest(tableData, 0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
