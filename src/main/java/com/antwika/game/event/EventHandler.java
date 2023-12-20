package com.antwika.game.event;

import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventHandler;
import com.antwika.game.core.IEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventHandler extends Thread implements IEventHandler {
    private final Logger logger = LoggerFactory.getLogger(EventHandler.class);

    private final BlockingQueue<IEvent> events = new LinkedBlockingQueue<>();

    private final List<IEventProcessor> eventProcessors;

    private boolean isRunning = true;

    public EventHandler(List<IEventProcessor> eventProcessors) {
        this.eventProcessors = eventProcessors;
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    @Override
    public synchronized void interrupt() {
        isRunning = false;
        super.interrupt();
    }

    public boolean offerEvent(IEvent event) {
        return events.offer(event);
    }

    protected BlockingQueue<IEvent> getEvents() {
        return events;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                tick();
                final IEvent event = events.poll(250L, TimeUnit.MILLISECONDS);

                if (event == null) {
                    continue;
                }

                onEvent(event);

                for (IEventProcessor eventProcessor : eventProcessors) {
                    if (!eventProcessor.canProcess(event)) continue;
                    eventProcessor.process(this, event);
                }
            }
        } catch (InterruptedException e) {
            logger.info("{} interrupted", this);
        }

        logger.info("{} thread ended", this);
    }

    public synchronized void onEvent(IEvent event) {

    }

    public synchronized void tick() {

    }
}
