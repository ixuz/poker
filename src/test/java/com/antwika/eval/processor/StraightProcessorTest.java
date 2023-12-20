package com.antwika.eval.processor;

import com.antwika.eval.processor.StraightProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StraightProcessorTest {
    @Test
    public void maxKickerCount() {
        final StraightProcessor straightProcessor = new StraightProcessor();
        assertEquals(1, straightProcessor.maxKickerCount());
    }
}
