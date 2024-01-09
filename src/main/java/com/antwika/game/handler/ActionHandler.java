package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActionHandler extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    private final IHandler handler;

    public ActionHandler(IHandler handler, String eventHandlerName, long eventPollTimeoutMillis) {
        super(eventHandlerName, eventPollTimeoutMillis);
        this.handler = handler;
    }

    @Override
    public synchronized void handle(IEvent event) {
        try {
            boolean handled = false;
            if (handler.canHandle(event)) {
                logger.debug("Handle: {}", event);

                final List<IEvent> additionalEvents = handler.handle(event);
                if (additionalEvents != null) {
                    for (IEvent additionalEvent : additionalEvents) {
                        offer(additionalEvent);
                    }
                }

                handled = true;
            }

            if (!handled) {
                logger.warn("No action handler could handle: {}", event);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void preEventHandle() {

    }

    @Override
    protected void noEventHandle() {

    }
}
