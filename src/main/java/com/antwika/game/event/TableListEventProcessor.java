package com.antwika.game.event;

import com.antwika.game.actor.Player;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventHandler;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.data.RequestTableListEvent;
import com.antwika.game.data.TableListEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableListEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(TableListEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof TableListEvent;
    }

    @Override
    public void process(IEventHandler handler, IEvent event) {
        final TableListEvent tableListEvent = (TableListEvent) event;

        if (handler instanceof Player) {
            final Player player = (Player) handler;
            player.onEvent(tableListEvent);
        }
    }
}
