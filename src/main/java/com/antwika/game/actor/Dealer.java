package com.antwika.game.actor;

import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.*;
import com.antwika.game.event.*;
import com.antwika.game.exception.GameException;
import com.antwika.game.util.DealerUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Dealer extends Actor {
    private static Logger logger = LoggerFactory.getLogger(Dealer.class);

    @Getter
    private ITableData table;

    public Dealer() {
        super("Dealer", List.of(
                new RequestDealerEventProcessor(),
                new RequestSeatEventProcessor(),
                new JoinEventProcessor(),
                new LeaveEventProcessor(),
                new AddChipsEventProcessor()
        ));
    }

    public void join(IActor actor, int seatIndex) throws GameException {
        DealerUtil.join(this, seatIndex, actor);
    }

    public void leave(IActor actor) throws GameException {
        DealerUtil.leave(this, actor);
    }

    @Override
    public void handleEvent(IEvent event) {
        if (event instanceof RequestDealerEvent) {
            final RequestDealerEvent requestDealerEvent = (RequestDealerEvent) event;
            final ITableData tableData = requestDealerEvent.getTableData();

            logger.info("{} is aware of {}", this, requestDealerEvent);

            event.getWorld().offerEvent(RequestDealerSeatEvent.builder()
                    .world(event.getWorld())
                    .actor(this)
                    .tableData(tableData)
                    .build());
        }
        if (event instanceof JoinEvent) {
            final JoinEvent joinEvent = (JoinEvent) event;
            final IActor actor = joinEvent.getActor();

            if (actor instanceof Dealer) {
                final Dealer dealer = (Dealer) joinEvent.getActor();
                if (dealer == this) {
                    logger.info("{} is aware of {}", this, joinEvent);

                    table = joinEvent.getTableData();
                }
            }
        }
    }
}
