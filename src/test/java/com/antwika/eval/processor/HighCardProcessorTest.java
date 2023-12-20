package com.antwika.eval.processor;

import com.antwika.eval.processor.HighCardProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HighCardProcessorTest {
    @Test
    public void maxKickerCount() {
        final HighCardProcessor highCardProcessor = new HighCardProcessor();
        assertEquals(5, highCardProcessor.maxKickerCount());
    }
}
