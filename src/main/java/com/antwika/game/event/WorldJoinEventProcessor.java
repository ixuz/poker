package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.Player;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.RequestDealerEvent;
import com.antwika.game.data.WorldJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldJoinEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(WorldJoinEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof WorldJoinEvent;
    }

    @Override
    public void process(IActor thisActor, IEvent event) {
        final WorldJoinEvent worldJoinEvent = (WorldJoinEvent) event;

        if (thisActor instanceof Player) {
            final Player player = (Player) thisActor;
            player.handleEvent(worldJoinEvent);
        }
    }
}
