package com.antwika.eval.processor;

import com.antwika.eval.processor.StraightFlushProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StraightFlushProcessorTest {
    @Test
    public void maxKickerCount() {
        final StraightFlushProcessor straightFlushProcessor = new StraightFlushProcessor();
        assertEquals(1, straightFlushProcessor.maxKickerCount());
    }
}
