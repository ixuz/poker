package com.antwika.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RankExceptionTest {
    @Test
    public void construct() {
        assertThrows(RankException.class, () -> { throw new RankException(); });
    }
}
