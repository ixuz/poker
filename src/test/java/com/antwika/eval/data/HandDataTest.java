package com.antwika.eval.data;

import com.antwika.eval.data.HandData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandDataTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        HandData.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("HandData(hand=0, kickers=null, handType=0, highCardKickerCount=0)", HandData.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getHand() {
        assertEquals(1L, HandData.builder().hand(1L).build().getHand());
    }

    @Test
    @Tag("UnitTest")
    public void getKickers() {
        int[] kickers = new int[]{};
        assertEquals(kickers, HandData.builder().kickers(kickers).build().getKickers());
    }

    @Test
    @Tag("UnitTest")
    public void getHandType() {
        assertEquals(1L, HandData.builder().handType(1L).build().getHandType());
    }

    @Test
    @Tag("UnitTest")
    public void getHighCardKickerCount() {
        assertEquals(1, HandData.builder().highCardKickerCount(1).build().getHighCardKickerCount());
    }
}
