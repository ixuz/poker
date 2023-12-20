package com.antwika.game;

import com.antwika.game.actor.Actor;
import com.antwika.game.core.IEvent;
import com.antwika.game.data.WorldJoinEvent;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ToString(onlyExplicitlyIncluded = true)
public class World extends Actor {
    private static Logger logger = LoggerFactory.getLogger(World.class);

    public List<Actor> actors = new ArrayList<>();

    @ToString.Include
    private final String worldName;

    public World(String worldName) {
        super(worldName, List.of());
        this.worldName = worldName;
    }

    @Override
    public synchronized void onEvent(IEvent event) {
        for (Actor actor : actors) {
            actor.offerEvent(event);
        }
    }

    @Override
    public synchronized void interrupt() {
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
    }

    public synchronized void add(Actor actor) {
        actors.add(actor);
        actor.start();
        logger.info("{} added {}", this, actor);

        offerEvent(WorldJoinEvent.builder()
                .world(this)
                .actor(actor)
                .build());
    }

    public synchronized void remove(Actor actor) {
        actor.interrupt();
        actors.remove(actor);
        logger.info("{} removed {}", this, actor);
    }
}
