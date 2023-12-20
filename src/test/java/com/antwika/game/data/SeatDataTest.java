package com.antwika.game.data;

import com.antwika.game.core.IActor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class SeatDataTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        SeatData.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("SeatData(actor=null, stack=0, committed=0)", SeatData.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getPlayer() {
        final IActor actor = mock(IActor.class);
        assertEquals(actor, SeatData.builder().actor(actor).build().getActor());
    }

    @Test
    @Tag("UnitTest")
    public void getStack() {
        assertEquals(1000, SeatData.builder().stack(1000).build().getStack());
    }

    @Test
    @Tag("UnitTest")
    public void getCommitted() {
        assertEquals(1000, SeatData.builder().committed(1000).build().getCommitted());
    }
}
