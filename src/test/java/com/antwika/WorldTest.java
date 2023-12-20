package com.antwika;

import com.antwika.game.World;
import com.antwika.game.actor.Dealer;
import com.antwika.game.actor.Player;
import com.antwika.game.actor.TableManager;
import com.antwika.game.core.ITableData;
import com.antwika.game.data.RequestSeatEvent;
import com.antwika.game.data.RequestTableOpenEvent;
import com.antwika.game.util.TableDataUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldTest {
    private static Logger logger = LoggerFactory.getLogger(WorldTest.class);

    @Test
    public void test() {
        final World world = new World("Antwika");
        world.start();

        final TableManager tableManager = new TableManager("TableManager");
        world.add(tableManager);

        final Dealer dealer = new Dealer();
        world.add(dealer);

        final Player alice = new Player("Alice");
        world.add(alice);

        final Player bob = new Player("Bob");
        world.add(bob);

        final ITableData tableData = TableDataUtil.createTableData(4, 10, 20, 0, 1);

        world.offerEvent(RequestTableOpenEvent.builder().world(world).tableData(tableData).build());

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("{}", tableData);

        world.interrupt();
    }
}
