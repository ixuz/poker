package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.Player;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.*;
import com.antwika.game.data.JoinEvent;
import com.antwika.game.data.RequestSeatEvent;
import com.antwika.game.data.RequestTableOpenEvent;
import com.antwika.game.exception.TableException;
import com.antwika.game.util.TableDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestTableOpenEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(RequestTableOpenEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof RequestTableOpenEvent;
    }

    @Override
    public void process(IEventHandler handler, IEvent event) {
        if (!(handler instanceof TableManager)) return;

        final RequestTableOpenEvent requestTableOpenEvent = (RequestTableOpenEvent) event;
        final ITableData tableData = requestTableOpenEvent.getTableData();

        final TableManager tableManager = (TableManager) handler;

        tableManager.onEvent(requestTableOpenEvent);
    }
}
