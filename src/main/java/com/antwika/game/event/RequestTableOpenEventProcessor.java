package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.Player;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.core.ITableData;
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
    public void process(IActor thisActor, IEvent event) {
        if (!(thisActor instanceof TableManager)) return;

        final RequestTableOpenEvent requestTableOpenEvent = (RequestTableOpenEvent) event;
        final ITableData tableData = requestTableOpenEvent.getTableData();

        final TableManager tableManager = (TableManager) thisActor;

        tableManager.handleEvent(requestTableOpenEvent);
    }
}
