package com.antwika.game.core;

import com.antwika.game.actor.Dealer;

public interface IEventProcessor {
    boolean canProcess(IEvent event);
    void process(IActor thisActor, IEvent event);
}
