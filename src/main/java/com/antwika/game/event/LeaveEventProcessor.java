package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventHandler;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.data.LeaveEvent;
import com.antwika.game.exception.GameException;
import com.antwika.game.util.DealerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaveEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(LeaveEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof LeaveEvent;
    }

    @Override
    public void process(IEventHandler handler, IEvent event) {
        try {
            final Dealer dealer = (Dealer) handler;
            final LeaveEvent joinEvent = (LeaveEvent) event;
            DealerUtil.leave(dealer, joinEvent.getActor());
            logger.info("{} left game {}", joinEvent.getActor(), dealer);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }
}
