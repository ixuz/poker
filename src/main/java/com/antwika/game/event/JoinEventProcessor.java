package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.Player;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventHandler;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.data.JoinEvent;
import com.antwika.game.exception.GameException;
import com.antwika.game.util.DealerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(JoinEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof JoinEvent;
    }

    @Override
    public void process(IEventHandler handler, IEvent event) {
        if (!(event instanceof JoinEvent)) return;

        final JoinEvent joinEvent = (JoinEvent) event;

        if (handler instanceof Player) {
            final Player player = (Player) handler;
            player.onEvent(joinEvent);
        }

        if (handler instanceof Dealer) {
            final Dealer dealer = (Dealer) handler;
            dealer.onEvent(joinEvent);
        }
    }
}
