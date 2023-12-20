package com.antwika.common.data;

import com.antwika.common.core.IHand;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;
import com.antwika.common.util.HandUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HandTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        Hand.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Hand(bitmask=0)", Hand.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getBitmask() throws BitmaskException {
        // Arrange
        final IHand hand = new Hand(1L);

        // Act
        final long handBitmask = hand.getBitmask();

        // Assert
        assertEquals(1L, handBitmask);
    }
}
