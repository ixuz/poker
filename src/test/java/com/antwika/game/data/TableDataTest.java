package com.antwika.game.data;

import com.antwika.game.core.IPot;
import com.antwika.game.core.ISeat;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class TableDataTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        TableData.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("TableData(seats=null, smallBlind=0, bigBlind=0, pots=null, buttonAt=0, actionAt=0)", TableData.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getSeats() {
        final List<ISeat> seats = mock(List.class);
        assertEquals(seats, TableData.builder().seats(seats).build().getSeats());
    }

    @Test
    @Tag("UnitTest")
    public void getSmallBlind() {
        assertEquals(1, TableData.builder().smallBlind(1).build().getSmallBlind());
    }

    @Test
    @Tag("UnitTest")
    public void getBigBlind() {
        assertEquals(1, TableData.builder().bigBlind(1).build().getBigBlind());
    }

    @Test
    @Tag("UnitTest")
    public void getPots() {
        final List<IPot> pots = mock(List.class);
        assertEquals(pots, TableData.builder().pots(pots).build().getPots());
    }

    @Test
    @Tag("UnitTest")
    public void getButtonAt() {
        assertEquals(1, TableData.builder().buttonAt(1).build().getButtonAt());
    }

    @Test
    @Tag("UnitTest")
    public void getActionAt() {
        assertEquals(1, TableData.builder().actionAt(1).build().getActionAt());
    }
}
