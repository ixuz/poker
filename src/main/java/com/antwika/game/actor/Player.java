package com.antwika.game.actor;

import com.antwika.game.World;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.*;
import com.antwika.game.event.JoinEventProcessor;
import com.antwika.game.event.TableListEventProcessor;
import com.antwika.game.event.WorldJoinEventProcessor;
import com.antwika.game.util.TableDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Player extends Actor {
    private static Logger logger = LoggerFactory.getLogger(Player.class);

    private World world;

    private ITableData tableData;

    private int seatIndex;

    private Long lastTableListRequestTimestamp = 0L;

    private int tableListRequestFrequency = 3;

    public Player(String name) {
        super(name, List.of(
                new WorldJoinEventProcessor(),
                new TableListEventProcessor(),
                new JoinEventProcessor()
        ));
    }

    @Override
    public void handleEvent(IEvent event) {
        if (event instanceof WorldJoinEvent) {
            final WorldJoinEvent worldJoinEvent = (WorldJoinEvent) event;
            if (worldJoinEvent.getActor() != this) return;

            logger.info("{} is aware of {}", this, worldJoinEvent);

            world = worldJoinEvent.getWorld();
        }
        if (event instanceof JoinEvent) {
            final JoinEvent joinEvent = (JoinEvent) event;
            if (joinEvent.getActor() != this) return;

            logger.info("{} is aware of {}", this, joinEvent);
            tableData = joinEvent.getTableData();
            seatIndex = joinEvent.getSeatIndex();
        }
        if (event instanceof TableListEvent) {
            final TableListEvent tableListEvent = (TableListEvent) event;

            logger.info("{} is aware of {}", this, tableListEvent);

            if (world == null) return;
            if (tableData == null) {

                final List<ITableData> tables = tableListEvent.getTables();

                if (!tables.isEmpty()) {
                    for (ITableData t : tables) {
                        final int seatIndex = TableDataUtil.findFirstAvailableSeatIndex(t);
                        if (seatIndex == -1) continue;

                        world.offerEvent(RequestSeatEvent.builder()
                                .world(world)
                                .actor(this)
                                .tableData(t)
                                .seatIndex(seatIndex)
                                .build());
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        if (world == null) return;

        final Long now = System.currentTimeMillis();

        if (tableData == null) {
            if (lastTableListRequestTimestamp + 1000/tableListRequestFrequency < now) {
                logger.info("{} Request new table list {}!", this, System.currentTimeMillis());
                lastTableListRequestTimestamp = now;

                world.offerEvent(RequestTableListEvent.builder()
                        .world(world)
                        .build());
            }
        }
    }
}
