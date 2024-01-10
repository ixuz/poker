package com.antwika.table.handler;

import com.antwika.table.event.IEvent;
import com.antwika.table.handler.hand.DealCommunityCardsRequestHandler;
import com.antwika.table.handler.hand.HandBeginRequestHandler;
import com.antwika.table.handler.hand.HandEndRequestHandler;
import com.antwika.table.handler.hand.ShowdownRequestHandler;
import com.antwika.table.handler.misc.AggregateHandler;
import com.antwika.table.handler.orbit.OrbitActionRequestHandler;
import com.antwika.table.handler.orbit.OrbitActionResponseHandler;
import com.antwika.table.handler.orbit.OrbitBeginRequestHandler;
import com.antwika.table.handler.orbit.OrbitEndRequestHandler;
import com.antwika.table.handler.player.PlayerJoinRequestHandler;
import com.antwika.table.handler.player.PlayerLeaveRequestHandler;

import java.util.List;

public class TexasHoldemHandler implements IHandler {
    private final IHandler handler;

    public TexasHoldemHandler() {
        this.handler = new AggregateHandler(List.of(
                new PlayerJoinRequestHandler(),
                new HandBeginRequestHandler(),
                new OrbitBeginRequestHandler(),
                new OrbitActionRequestHandler(),
                new OrbitActionResponseHandler(),
                new DealCommunityCardsRequestHandler(),
                new OrbitEndRequestHandler(),
                new ShowdownRequestHandler(),
                new HandEndRequestHandler(),
                new PlayerLeaveRequestHandler()
        ));
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
