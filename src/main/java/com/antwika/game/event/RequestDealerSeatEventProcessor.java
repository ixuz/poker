package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.*;
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
    public void process(IEventHandler handler, IEvent event) {
        if (!(handler instanceof TableManager)) return;

        final RequestDealerSeatEvent requestDealerSeatEvent = (RequestDealerSeatEvent) event;

        final TableManager tableManager = (TableManager) handler;
        tableManager.onEvent(requestDealerSeatEvent);
    }
}
