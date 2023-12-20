package com.antwika.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SuitExceptionTest {
    @Test
    public void construct() {
        assertThrows(SuitException.class, () -> { throw new SuitException(); });
    }
}
