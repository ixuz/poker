package com.antwika.table;

import com.antwika.table.data.DeckData;
import com.antwika.table.data.PotData;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

public class TableDataTest {
    @Test
    public void equalityWhenBothAreFreshInstances() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        assertEquals(table1, table2);
    }

    @Test
    public void inequalityWhenTableNameDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setTableName("A");
        table2.setTableName("B");
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenGameTypeDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setGameType("A");
        table2.setGameType("B");
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenGameStageDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setGameStage(TableData.GameStage.NONE);
        table2.setGameStage(TableData.GameStage.PREFLOP);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenSeatsDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        final var seat1 = mock(SeatData.class);
        final var seat2 = mock(SeatData.class);
        table1.setSeats(new ArrayList<>());
        table2.setSeats(new ArrayList<>());
        table1.getSeats().add(seat1);
        table2.getSeats().add(seat2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenPotsDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        final var pot1 = mock(PotData.class);
        final var pot2 = mock(PotData.class);
        table1.getPots().add(pot1);
        table2.getPots().add(pot2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenDeckDataDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        final var deckData1 = mock(DeckData.class);
        final var deckData2 = mock(DeckData.class);
        table1.setDeckData(deckData1);
        table2.setDeckData(deckData2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenSmallBlindDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setSmallBlind(1);
        table2.setSmallBlind(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenBigBlindDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setBigBlind(1);
        table2.setBigBlind(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenHandIdDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setHandId(1L);
        table2.setHandId(2L);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenButtonAtDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setButtonAt(1);
        table2.setButtonAt(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenActionAtDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setActionAt(1);
        table2.setActionAt(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenTotalBetDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setTotalBet(1);
        table2.setTotalBet(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenLastRaiseDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setLastRaise(1);
        table2.setLastRaise(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenCardsDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setCards(1L);
        table2.setCards(2L);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenDeliveredDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setDelivered(1);
        table2.setDelivered(2);
        assertNotEquals(table1, table2);
    }

    @Test
    public void inequalityWhenChipsInPlayDiffers() {
        final var table1 = new TableData();
        final var table2 = new TableData();
        table1.setChipsInPlay(1);
        table2.setChipsInPlay(2);
        assertNotEquals(table1, table2);
    }
}
