package com.antwika.game.core;

public interface IEventHandler extends Runnable {
    boolean offerEvent(IEvent event);
    void onEvent(IEvent event);
    void tick();
}
