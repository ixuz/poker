package com.antwika.game.handler;

import com.antwika.game.Event;

public interface IActionHandler {
    boolean canHandle(Event event);
    void handle(Event event);
}
