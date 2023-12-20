package com.antwika.eval.processor;

import com.antwika.eval.processor.QuadsProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuadsProcessorTest {
    @Test
    public void maxKickerCount() {
        final QuadsProcessor quadsProcessor = new QuadsProcessor();
        assertEquals(2, quadsProcessor.maxKickerCount());
    }
}
