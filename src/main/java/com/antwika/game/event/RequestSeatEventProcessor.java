package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.AddChipsEvent;
import com.antwika.game.data.JoinEvent;
import com.antwika.game.data.RequestSeatEvent;
import com.antwika.game.exception.TableException;
import com.antwika.game.util.TableDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestSeatEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(RequestSeatEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof RequestSeatEvent;
    }

    @Override
    public void process(IActor thisActor, IEvent event) {
        final RequestSeatEvent requestSeatEvent = (RequestSeatEvent) event;
        final IActor actor = requestSeatEvent.getActor();
        final ITableData tableData = requestSeatEvent.getTableData();
        final int seatIndex = requestSeatEvent.getSeatIndex();

        try {
            final Dealer dealer = (Dealer) thisActor;

            if (dealer.getTable() != tableData) return;

            TableDataUtil.seatPlayer(tableData, actor, seatIndex);

            logger.info("{} joined table {} at seat #{}", actor, tableData, seatIndex);

            event.getWorld().offerEvent(JoinEvent.builder()
                    .actor(actor)
                    .tableData(tableData)
                    .seatIndex(seatIndex)
                    .build());
        } catch (TableException e) {
            logger.info("{} could not join table {} at seat #{}", actor, tableData, seatIndex);
        }
    }
}
