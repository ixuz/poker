package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AggregateHandler extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AggregateHandler.class);

    private static final List<IHandler> handlers = List.of(
            new ShowdownRequestHandler(),
            new HandBeginRequestHandler(),
            new PlayerJoinRequestHandler(),
            new PlayerLeaveRequestHandler(),
            new OrbitBeginRequestHandler(),
            new OrbitEndRequestHandler(),
            new OrbitActionRequestHandler(),
            new OrbitActionResponseHandler(),
            new DealCommunityCardsRequestHandler()
    );

    public AggregateHandler(String eventHandlerName, long eventPollTimeoutMillis) {
        super(eventHandlerName, eventPollTimeoutMillis);
    }

    @Override
    public synchronized void handle(IEvent event) {
        try {
            boolean handled = false;
            for (IHandler actionHandler : handlers) {
                if (actionHandler.canHandle(event)) {
                    logger.debug("Handle: {}", event);
                    final List<IEvent> additionalEvents = actionHandler.handle(event);
                    if (additionalEvents != null) {
                        for (IEvent additionalEvent : additionalEvents) {
                            offer(additionalEvent);
                        }
                    }

                    handled = true;
                }
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
