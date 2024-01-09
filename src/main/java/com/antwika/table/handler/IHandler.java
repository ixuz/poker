package com.antwika.table.handler;

import com.antwika.table.event.IEvent;

import java.util.List;

public interface IHandler {
    boolean canHandle(IEvent event);
    List<IEvent> handle(IEvent event);
}
