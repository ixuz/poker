package com.antwika.eval.processor;

import com.antwika.eval.processor.TwoPairProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoPairProcessorTest {
    @Test
    public void maxKickerCount() {
        final TwoPairProcessor twoPairProcessor = new TwoPairProcessor();
        assertEquals(3, twoPairProcessor.maxKickerCount());
    }
}
