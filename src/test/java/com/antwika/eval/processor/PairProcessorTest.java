package com.antwika.eval.processor;

import com.antwika.eval.processor.PairProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PairProcessorTest {
    @Test
    public void maxKickerCount() {
        final PairProcessor pairProcessor = new PairProcessor();
        assertEquals(4, pairProcessor.maxKickerCount());
    }
}
