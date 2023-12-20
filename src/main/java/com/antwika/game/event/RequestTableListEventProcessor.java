package com.antwika.game.event;

import com.antwika.game.actor.TableManager;
import com.antwika.game.core.*;
import com.antwika.game.data.RequestTableListEvent;
import com.antwika.game.data.RequestTableOpenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTableListEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(RequestTableListEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof RequestTableListEvent;
    }

    @Override
    public void process(IEventHandler handler, IEvent event) {
        if (!(handler instanceof TableManager)) return;

        final RequestTableListEvent requestTableListEvent = (RequestTableListEvent) event;

        final TableManager tableManager = (TableManager) handler;

        tableManager.onEvent(requestTableListEvent);
    }
}
