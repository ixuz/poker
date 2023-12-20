package com.antwika.game.actor;

import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@ToString(onlyExplicitlyIncluded = true)
public class Actor extends Thread implements IActor {
    @Getter
    @ToString.Include
    private final String actorName;

    private final Logger logger = LoggerFactory.getLogger(Actor.class);

    private boolean isRunning = true;

    private final BlockingQueue<IEvent> events = new LinkedBlockingQueue<>();

    private final List<IEventProcessor> eventProcessors;

    public Actor(String actorName, List<IEventProcessor> eventProcessors) {
        this.actorName = actorName;
        this.eventProcessors = eventProcessors;
    }

    @Override
    public synchronized void interrupt() {
        isRunning = false;
        super.interrupt();
    }

    private synchronized boolean isRunning() {
        return isRunning;
    }

    public boolean offerEvent(IEvent event) {
        return events.offer(event);
    }

    public void handleEvent(IEvent event) {

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

    protected void tick() {

    }
}
