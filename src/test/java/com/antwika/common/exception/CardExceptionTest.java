package com.antwika.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardExceptionTest {
    @Test
    public void construct() {
        assertThrows(CardException.class, () -> { throw new CardException(); });
    }
}
