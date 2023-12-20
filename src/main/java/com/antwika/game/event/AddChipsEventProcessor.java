package com.antwika.game.event;

import com.antwika.game.actor.Dealer;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.IEventProcessor;
import com.antwika.game.data.AddChipsEvent;
import com.antwika.game.exception.TableException;
import com.antwika.game.util.TableDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddChipsEventProcessor implements IEventProcessor {
    private final Logger logger = LoggerFactory.getLogger(AddChipsEventProcessor.class);

    @Override
    public boolean canProcess(IEvent event) {
        return event instanceof AddChipsEvent;
    }

    @Override
    public void process(IActor thisActor, IEvent event) {
        final AddChipsEvent addChipsEvent = (AddChipsEvent) event;
        final IActor actor = addChipsEvent.getActor();
        final int seatIndex = addChipsEvent.getSeatIndex();
        final int amount = addChipsEvent.getAmount();

        try {
            final Dealer dealer = (Dealer) thisActor;
            TableDataUtil.addChipsToSeat(dealer.getTable(), actor, seatIndex, amount);

            logger.info("Player {} at seat {} added {} chips",
                    actor,
                    seatIndex,
                    amount);
        } catch (TableException e) {
            throw new RuntimeException(e);
        }
    }
}
