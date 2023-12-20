package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.JoinEvent;
import com.antwika.game.data.RequestDealerSeatEvent;
import com.antwika.game.data.RequestSeatEvent;
import com.antwika.game.exception.TableException;
import com.antwika.game.util.TableDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDealerSeatEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(RequestDealerSeatEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof RequestDealerSeatEvent;
    }

    @Override
    public void process(IActor thisActor, IEvent event) {
        if (!(thisActor instanceof TableManager)) return;

        final RequestDealerSeatEvent requestDealerSeatEvent = (RequestDealerSeatEvent) event;

        final TableManager tableManager = (TableManager) thisActor;
        tableManager.handleEvent(requestDealerSeatEvent);
    }
}
