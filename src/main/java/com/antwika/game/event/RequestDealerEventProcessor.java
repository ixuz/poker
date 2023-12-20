package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.RequestDealerEvent;
import com.antwika.game.data.RequestTableOpenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDealerEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(RequestDealerEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof RequestDealerEvent;
    }

    @Override
    public void process(IActor thisActor, IEvent event) {
        if (!(thisActor instanceof Dealer)) return;

        final RequestDealerEvent requestDealerEvent = (RequestDealerEvent) event;
        final ITableData tableData = requestDealerEvent.getTableData();

        final Dealer dealer = (Dealer) thisActor;

        dealer.handleEvent(requestDealerEvent);
    }
}
