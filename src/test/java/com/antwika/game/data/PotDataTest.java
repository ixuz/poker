package com.antwika.game.data;

import com.antwika.game.core.IActor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class PotDataTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        PotData.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("PotData(actors=null, amount=0)", PotData.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getPlayers() {
        final List<IActor> actors = mock(List.class);
        assertEquals(actors, PotData.builder().actors(actors).build().getActors());
    }

    @Test
    @Tag("UnitTest")
    public void getAmount() {
        assertEquals(1000, PotData.builder().amount(1000).build().getAmount());
    }
}
