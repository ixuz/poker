package com.antwika.common.data;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RankTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        Rank.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Rank(value=0)", Rank.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getValue() {
        // Arrange
        final Rank rank = new Rank(1);

        // Act
        final int rankValue = rank.getValue();

        // Assert
        assertEquals(1, rankValue);
    }
}
