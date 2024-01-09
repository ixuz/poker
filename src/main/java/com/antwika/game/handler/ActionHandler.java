package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActionHandler extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    private static final List<IHandler> actionHandlers = List.of(
            new StartHandRequestHandler(),
            new FoldHandler(),
            new CheckHandler(),
            new CallHandler(),
            new BetHandler(),
            new RaiseHandler(),
            new ShowdownRequestHandler(),
            new DealCardsHandler(),
            new HandBeginRequestHandler(),
            new PlayerJoinRequestHandler(),
            new PlayerJoinHandler(),
            new PlayerLeaveHandler(),
            new BeginBettingRoundRequestHandler(),
            new EndBettingRoundRequestHandler(),
            new BettingRoundPlayerActionRequestHandler(),
            new ShowdownEventHandler(),
            new HandBeginEventHandler(),
            new DealCommunityCardsRequestHandler()
    );

    public ActionHandler(String eventHandlerName, long eventPollTimeoutMillis) {
        super(eventHandlerName, eventPollTimeoutMillis);
    }

    public synchronized void handleEvent(IEvent event) {
        try {
            boolean handled = false;
            for (IHandler actionHandler : actionHandlers) {
                if (actionHandler.canHandle(event)) {
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
    public synchronized void handle(IEvent event) {
        handleEvent(event);
    }
}
