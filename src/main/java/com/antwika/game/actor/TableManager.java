package com.antwika.game.actor;

import com.antwika.game.core.IEvent;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.*;
import com.antwika.game.event.RequestDealerSeatEventProcessor;
import com.antwika.game.event.RequestTableListEventProcessor;
import com.antwika.game.event.RequestTableOpenEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TableManager extends Actor {
    private static Logger logger = LoggerFactory.getLogger(TableManager.class);

    private List<ITableData> tables = new ArrayList<>();

    public TableManager(String actorName) {
        super(actorName, List.of(
                // new RequestTableOpenEventProcessor(),
                // new RequestDealerSeatEventProcessor()
                // new RequestTableListEventProcessor()
        ));
    }

    @Override
    public void onEvent(IEvent event) {
        if (event instanceof RequestTableOpenEvent) {
            final RequestTableOpenEvent requestTableOpenEvent = (RequestTableOpenEvent) event;
            final ITableData tableData = requestTableOpenEvent.getTableData();

            logger.info("{} is aware of {}", this, requestTableOpenEvent);

            tables.add(tableData);

            event.getWorld().offerEvent(TableOpenEvent.builder()
                    .world(event.getWorld())
                    .tableData(tableData)
                    .build());

            event.getWorld().offerEvent(RequestDealerEvent.builder()
                    .world(event.getWorld())
                    .tableData(tableData)
                    .build());
        }
        if (event instanceof RequestDealerSeatEvent) {
            final RequestDealerSeatEvent requestDealerSeatEvent = (RequestDealerSeatEvent) event;

            logger.info("{} is aware of {}", this, requestDealerSeatEvent);

            final int foundTableDataIndex = tables.indexOf(requestDealerSeatEvent.getTableData());
            if (foundTableDataIndex == -1) return;

            final ITableData tableData = tables.get(foundTableDataIndex);

            final Dealer dealer = (Dealer) requestDealerSeatEvent.getActor();

            tableData.setDealer(dealer);

            event.getWorld().offerEvent(JoinEvent.builder()
                    .world(event.getWorld())
                    .tableData(tableData)
                    .actor(dealer)
                    .build());
        }
        if (event instanceof RequestTableListEvent) {
            final RequestTableListEvent requestTableListEvent = (RequestTableListEvent) event;

            logger.info("{} is aware of {}", this, requestTableListEvent);

            event.getWorld().offerEvent(TableListEvent.builder()
                    .world(requestTableListEvent.getWorld())
                    .actor(requestTableListEvent.getActor())
                    .tables(tables)
                    .build());
        }
    }
}
