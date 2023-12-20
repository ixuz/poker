package com.antwika.common.data;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SuitTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        Suit.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Suit(value=0)", Suit.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getValue() {
        // Arrange
        final Suit suit = new Suit(1);

        // Act & Assert
        assertEquals(1, suit.getValue());
    }
}
