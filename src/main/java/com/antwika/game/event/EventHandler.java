package com.antwika.game.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    private final BlockingQueue<IEvent> events = new LinkedBlockingQueue<>();
    private boolean running = false;

    public synchronized boolean offer(IEvent event) throws InterruptedException {
        return events.offer(event, 1L, TimeUnit.SECONDS);
    }

    public synchronized void handle(IEvent event) {

    }

    public synchronized void stopThread() {
        logger.debug("Stopping {}", this);
        running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                final IEvent event = events.poll(1L, TimeUnit.SECONDS);
                if (event != null) {
                    handle(event);
                }
            } catch (InterruptedException e) {
                logger.error("Thread interrupted", e);
                stopThread();

            }
        }
    }
}
