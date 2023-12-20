package com.antwika.game.event;

import com.antwika.game.actor.TableManager;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.core.ITableData;
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
    public void process(IActor thisActor, IEvent event) {
        if (!(thisActor instanceof TableManager)) return;

        final RequestTableListEvent requestTableListEvent = (RequestTableListEvent) event;

        final TableManager tableManager = (TableManager) thisActor;

        tableManager.handleEvent(requestTableListEvent);
    }
}
