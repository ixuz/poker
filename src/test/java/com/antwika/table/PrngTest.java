package com.antwika.table;

import com.antwika.table.common.Prng;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrngTest {
    @Test
    public void nextInt_withNoArguments_incrementSteps() {
        final Prng prng = new Prng(1L);
        assertEquals(0L, prng.getSteps());
        prng.nextInt();
        assertEquals(1L, prng.getSteps());
        prng.nextInt();
        assertEquals(2L, prng.getSteps());
    }

    @Test
    public void nextInt_withBound_incrementSteps() {
        final Prng prng = new Prng(1L);
        assertEquals(0L, prng.getSteps());
        prng.nextInt(100);
        assertEquals(1L, prng.getSteps());
        prng.nextInt(100);
        assertEquals(2L, prng.getSteps());
    }

    @Test
    public void nextInt_whenPriorNextIntCallsHaveBeenMadeWithVariousArguments_stillResultsInSameValueTheNthTime() {
        final Prng prng1 = new Prng(1L);
        prng1.nextInt();
        prng1.nextInt(10);
        prng1.nextInt(100);
        prng1.nextInt();
        prng1.nextInt(25);
        final int sample1 = prng1.nextInt();

        final Prng prng2 = new Prng(1L);
        prng2.nextInt(10);
        prng2.nextInt();
        prng2.nextInt(10);
        prng2.nextInt(44);
        prng2.nextInt();
        final int sample2 = prng2.nextInt();

        assertEquals(sample1, sample2);
    }
}
