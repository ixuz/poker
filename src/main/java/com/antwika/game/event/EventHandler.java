package com.antwika.game.event;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class EventHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    private final BlockingQueue<IEvent> events = new LinkedBlockingQueue<>();

    public enum EventHandlerState { NONE, STARTING, STARTED, STOPPING, STOPPED }
    private EventHandlerState eventHandlerState = EventHandlerState.NONE;
    private boolean running = false;

    @Getter
    private String eventHandlerName;

    @Getter
    private long eventPollTimeoutMillis;

    public EventHandler(String eventHandlerName, long eventPollTimeoutMillis) {
        super();
        this.eventHandlerName = eventHandlerName;
        this.eventPollTimeoutMillis = eventPollTimeoutMillis;
    }

    protected void setEventHandlerState(EventHandlerState eventHandlerState) {
        if (!this.eventHandlerState.equals(eventHandlerState)) {
            this.eventHandlerState = eventHandlerState;
            logger.info("EventHandlerState[{}] set to \"{}\"", getEventHandlerName(), getEventHandlerState());
        }
    }

    protected EventHandlerState getEventHandlerState() {
        return eventHandlerState;
    }

    public synchronized boolean offer(IEvent event) throws InterruptedException {
        return events.offer(event, 1L, TimeUnit.SECONDS);
    }

    public synchronized void handle(IEvent event) {

    }

    public synchronized void stopThread() {
        setEventHandlerState(EventHandlerState.STOPPING);
    }

    protected synchronized void setRunning(boolean running) {
        this.running = running;
    }

    protected synchronized boolean isRunning() {
        return running;
    }

    abstract protected void preEventHandle();

    abstract protected void noEventHandle();

    @Override
    public void run() {
        setEventHandlerState(EventHandlerState.STARTING);
        setRunning(true);

        setEventHandlerState(EventHandlerState.STARTED);
        while (getEventHandlerState().equals(EventHandlerState.STARTED)) {
            try {
                preEventHandle();
                final IEvent event = events.poll(getEventPollTimeoutMillis(), TimeUnit.MILLISECONDS);
                if (event != null) {
                    handle(event);
                } else {
                    noEventHandle();
                }
            } catch (InterruptedException e) {
                logger.error("Thread interrupted", e);
                break;
            }
        }

        setEventHandlerState(EventHandlerState.STOPPING);
        setRunning(false);

        setEventHandlerState(EventHandlerState.STOPPED);
        // stopThread();
    }
}
