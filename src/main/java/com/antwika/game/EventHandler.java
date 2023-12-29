package com.antwika.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class EventHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
    private final BlockingQueue<Event> events = new LinkedBlockingQueue<>();
    private boolean running = false;

    public synchronized boolean offer(Event event) throws InterruptedException {
        return events.offer(event, 1L, TimeUnit.SECONDS);
    }

    public synchronized Event handle(Event event) {
        return null;
    }

    public synchronized void stopThread() {
        running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            stopThread();
        }
    }
}
