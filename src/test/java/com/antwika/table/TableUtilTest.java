package com.antwika.table;

import com.antwika.table.event.*;
import com.antwika.table.handler.TexasHoldemHandler;
import com.antwika.table.player.Player;
import com.antwika.table.player.PremiumPlayer;
import com.antwika.table.player.RandomPlayer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TableUtilTest {
    @Test
    @Disabled
    public void test() throws InterruptedException {
        final Table table = new Table(new TexasHoldemHandler(), 10L, 100L);
        table.start();

        final Player player1 = new PremiumPlayer(1L, "Alice");
        final Player player2 = new PremiumPlayer(2L, "Bob");
        final Player player3 = new PremiumPlayer(3L, "Charlie");
        final Player player4 = new RandomPlayer(4L, "David");
        final Player player5 = new RandomPlayer(5L, "Eric");
        final Player player6 = new RandomPlayer(6L, "Filipe");

        table.offer(new PlayerJoinRequest(table.getTableData(), table.getTableData().getSeats().get(0), player1, 1000));
        table.offer(new PlayerJoinRequest(table.getTableData(), table.getTableData().getSeats().get(1), player2, 1000));
        table.offer(new PlayerJoinRequest(table.getTableData(), table.getTableData().getSeats().get(2), player3, 1000));
        table.offer(new PlayerJoinRequest(table.getTableData(), table.getTableData().getSeats().get(3), player4, 1000));
        table.offer(new PlayerJoinRequest(table.getTableData(), table.getTableData().getSeats().get(4), player5, 1000));
        table.offer(new PlayerJoinRequest(table.getTableData(), table.getTableData().getSeats().get(5), player6, 1000));

        table.join();
    }
}
