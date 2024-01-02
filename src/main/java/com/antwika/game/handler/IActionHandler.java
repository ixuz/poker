package com.antwika.game.handler;

import com.antwika.game.event.IEvent;

public interface IActionHandler {
    boolean canHandle(IEvent event);
    void handle(IEvent event);
}
