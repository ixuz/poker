package com.antwika.game.handler;

import com.antwika.game.event.IEvent;

import java.util.List;

public interface IHandler {
    boolean canHandle(IEvent event);
    List<IEvent> handle(IEvent event);
}
