package com.antwika.table.handler;

import com.antwika.table.data.TableData;
import com.antwika.table.event.OrbitBeginRequest;
import com.antwika.table.event.DealCommunityCardsRequest;
import com.antwika.table.event.IEvent;
import com.antwika.table.util.TableUtil;
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
            final TableData tableData = dealCommunityCardsRequest.getTableData();

            TableUtil.dealCommunityCards(tableData, dealCommunityCardsRequest.getCount());

            return List.of(new OrbitBeginRequest(tableData, 0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
