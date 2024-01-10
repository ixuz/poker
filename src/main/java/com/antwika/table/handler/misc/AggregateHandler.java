package com.antwika.table.handler.misc;

import com.antwika.table.event.IEvent;
import com.antwika.table.handler.IHandler;
import java.util.ArrayList;
import java.util.List;

public class AggregateHandler implements IHandler {
    private final List<IHandler> handlers;

    public AggregateHandler(List<IHandler> handlers) {
        this.handlers = handlers;
    }

    public boolean canHandle(IEvent event) {
        for (IHandler handler : handlers) {
            if (handler.canHandle(event)) return true;
        }
        return false;
    }

    public List<IEvent> handle(IEvent event) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        for (IHandler handler : handlers) {
            if (handler.canHandle(event)) {
                final List<IEvent> result = handler.handle(event);
                if (result != null) {
                    additionalEvents.addAll(result);
                }
            }
        }

        return additionalEvents;
    }
}
