package com.antwika.table.handler;

import com.antwika.table.event.IEvent;

import java.util.List;

public class TexasHoldemHandler implements IHandler {
    private IHandler handler;

    public TexasHoldemHandler() {
        this.handler = new AggregateHandler(List.of(
                new ShowdownRequestHandler(),
                new HandBeginRequestHandler(),
                new PlayerJoinRequestHandler(),
                new PlayerLeaveRequestHandler(),
                new OrbitBeginRequestHandler(),
                new OrbitEndRequestHandler(),
                new OrbitActionRequestHandler(),
                new OrbitActionResponseHandler(),
                new DealCommunityCardsRequestHandler()
        ));;
    }

    @Override
    public boolean canHandle(IEvent event) {
        return handler.canHandle(event);
    }

    @Override
    public List<IEvent> handle(IEvent event) {
        return handler.handle(event);
    }
}
