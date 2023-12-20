package com.antwika.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class HandExceptionTest {
    @Test
    public void construct() {
        assertThrows(HandException.class, () -> { throw new HandException(); });
    }
}
