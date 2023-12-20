package com.antwika.game;

import com.antwika.game.actor.Actor;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.data.WorldJoinEvent;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@ToString(onlyExplicitlyIncluded = true)
public class World extends Thread {
    private static Logger logger = LoggerFactory.getLogger(World.class);

    public List<Actor> actors = new ArrayList<>();

    @ToString.Include
    private final String worldName;

    private final BlockingQueue<IEvent> events = new LinkedBlockingQueue<>();

    private boolean isRunning = true;

    public World(String worldName) {
        this.worldName = worldName;
    }

    @Override
    public synchronized void start() {
        logger.info("{} starting...", this);
        super.start();
        logger.info("{} started", this);
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                final IEvent event = events.poll(1L, TimeUnit.SECONDS);

                if (event == null) {
                    continue;
                }

                for (Actor actor : actors) {
                    actor.offerEvent(event);
                }
            }
        } catch (InterruptedException e) {
            logger.info("{} interrupted", this);
        }

        logger.info("{} thread ended", this);
    }

    @Override
    public synchronized void interrupt() {
        logger.info("{} interrupting...", this);
        final List<Actor> copyOfActors = List.copyOf(actors);
        for (Actor actor : copyOfActors) {
            remove(actor);
        }

        for (Actor actor : copyOfActors) {
            try {
                actor.join(5000);
            } catch (InterruptedException e) {
                logger.warn("{} attempted to interrupt actors but timeout exceeded", this);
            }
        }

        super.interrupt();
        logger.info("{} interrupted", this);
    }

    public synchronized void add(Actor actor) {
        logger.info("{} adding {}...", this, actor);
        actors.add(actor);
        actor.start();
        logger.info("{} added {}", this, actor);

        offerEvent(WorldJoinEvent.builder()
                .world(this)
                .actor(actor)
                .build());
    }

    public synchronized void remove(Actor actor) {
        logger.info("{} removing {}...", this, actor);
        actor.interrupt();
        actors.remove(actor);
        logger.info("{} removed {}", this, actor);
    }

    public boolean offerEvent(IEvent event) {
        return events.offer(event);
    }
}
