package com.antwika.game.actor;

import com.antwika.game.core.IActor;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.event.EventHandler;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString(onlyExplicitlyIncluded = true)
public class Actor extends EventHandler implements IActor {
    @Getter
    @ToString.Include
    private final String actorName;

    public Actor(String actorName, List<IEventProcessor> eventProcessors) {
        super(eventProcessors);
        this.actorName = actorName;
    }
}
