package com.antwika.table.game;

import com.antwika.table.TableDataFactory;
import com.antwika.table.data.TableData;
import com.antwika.table.event.*;
import com.antwika.table.handler.*;
import com.antwika.table.util.TableUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Getter
public class Table extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Table.class);

    private final TableData tableData;

    private final long maxHandCount;

    boolean shouldStopAfterHand = false;

    private final IHandler handler;

    public Table(long maxHandCount, long eventPollTimeoutMillis) {
        super("Game", eventPollTimeoutMillis);
        this.handler = new AggregateHandler(List.of(
                new ShowdownRequestHandler(),
                new HandBeginRequestHandler(),
                new PlayerJoinRequestHandler(),
                new PlayerLeaveRequestHandler(),
                new OrbitBeginRequestHandler(),
                new OrbitEndRequestHandler(),
                new OrbitActionRequestHandler(),
                new OrbitActionResponseHandler(),
                new DealCommunityCardsRequestHandler()
        ));
        this.maxHandCount = maxHandCount;
        tableData = TableDataFactory.createTableData(1L, "Lacuna I", 6, 5, 10);
    }

    @Override
    public synchronized void stopThread() {
        shouldStopAfterHand = true;
    }

    @Override
    public synchronized void handle(IEvent event) {
        try {
            boolean handled = false;
            if (handler.canHandle(event)) {
                logger.debug("Handle: {}", event);

                final List<IEvent> additionalEvents = handler.handle(event);
                if (additionalEvents != null) {
                    for (IEvent additionalEvent : additionalEvents) {
                        offer(additionalEvent);
                    }
                }

                handled = true;
            }

            if (!handled) {
                logger.warn("No action handler could handle: {}", event);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected synchronized void noEventHandle() {
        try {
            if (tableData.getGameStage().equals(TableData.GameStage.NONE)) {
                if (TableUtil.canStartHand(tableData)) {
                    if (tableData.getHandId() >= maxHandCount) {
                        shouldStopAfterHand = true;
                    }
                    if (shouldStopAfterHand) {
                        super.stopThread();
                        return;
                    }
                    offer(new HandBeginRequest(tableData));
                }
            }
        } catch (InterruptedException e) {
            logger.info("Interrupted", e);
        }
    }

    @Override
    protected void preEventHandle() {

    }
}
