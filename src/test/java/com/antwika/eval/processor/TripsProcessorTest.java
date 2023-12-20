package com.antwika.eval.processor;

import com.antwika.eval.processor.TripsProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripsProcessorTest {
    @Test
    public void maxKickerCount() {
        final TripsProcessor tripsProcessor = new TripsProcessor();
        assertEquals(3, tripsProcessor.maxKickerCount());
    }
}
