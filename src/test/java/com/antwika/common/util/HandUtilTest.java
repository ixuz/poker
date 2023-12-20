package com.antwika.common.util;

import com.antwika.common.data.Hand;
import com.antwika.common.core.IHand;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;
import com.antwika.common.util.CardUtil;
import com.antwika.common.util.HandUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HandUtilTest {
    @Test
    @Tag("IntegrationTest")
    public void fromNotation() throws NotationException, BitmaskException {
        assertEquals(0b0000000000000000000000000000000000000000000000000001L, HandUtil.fromNotation("2c").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000000000000000010L, HandUtil.fromNotation("3c").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000000000000000100L, HandUtil.fromNotation("4c").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000000010000000000L, HandUtil.fromNotation("Qc").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000000100000000000L, HandUtil.fromNotation("Kc").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000001000000000000L, HandUtil.fromNotation("Ac").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000010000000000000L, HandUtil.fromNotation("2d").getBitmask());
        assertEquals(0b0000000000000000000000000000000000000100000000000000L, HandUtil.fromNotation("3d").getBitmask());
        assertEquals(0b0000000000000000000000000000000000001000000000000000L, HandUtil.fromNotation("4d").getBitmask());
        assertEquals(0b0000000000000000000000000000100000000000000000000000L, HandUtil.fromNotation("Qd").getBitmask());
        assertEquals(0b0000000000000000000000000001000000000000000000000000L, HandUtil.fromNotation("Kd").getBitmask());
        assertEquals(0b0000000000000000000000000010000000000000000000000000L, HandUtil.fromNotation("Ad").getBitmask());
        assertEquals(0b0000000000000000000000000100000000000000000000000000L, HandUtil.fromNotation("2h").getBitmask());
        assertEquals(0b0000000000000000000000001000000000000000000000000000L, HandUtil.fromNotation("3h").getBitmask());
        assertEquals(0b0000000000000000000000010000000000000000000000000000L, HandUtil.fromNotation("4h").getBitmask());
        assertEquals(0b0000000000000001000000000000000000000000000000000000L, HandUtil.fromNotation("Qh").getBitmask());
        assertEquals(0b0000000000000010000000000000000000000000000000000000L, HandUtil.fromNotation("Kh").getBitmask());
        assertEquals(0b0000000000000100000000000000000000000000000000000000L, HandUtil.fromNotation("Ah").getBitmask());
        assertEquals(0b0000000000001000000000000000000000000000000000000000L, HandUtil.fromNotation("2s").getBitmask());
        assertEquals(0b0000000000010000000000000000000000000000000000000000L, HandUtil.fromNotation("3s").getBitmask());
        assertEquals(0b0000000000100000000000000000000000000000000000000000L, HandUtil.fromNotation("4s").getBitmask());
        assertEquals(0b0010000000000000000000000000000000000000000000000000L, HandUtil.fromNotation("Qs").getBitmask());
        assertEquals(0b0100000000000000000000000000000000000000000000000000L, HandUtil.fromNotation("Ks").getBitmask());
        assertEquals(0b1000000000000000000000000000000000000000000000000000L, HandUtil.fromNotation("As").getBitmask());
    }

    @Test
    @Tag("IntegrationTest")
    public void fromNotation_whenProvidedInvalidNotation_throwsNotationException() {
        assertThrows(NotationException.class, () -> HandUtil.fromNotation("Xx"));
    }

    @Test
    public void toNotation() throws NotationException {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            staticCardUtil.when(() -> CardUtil.toNotation(0, 0)).thenReturn("Xx");
            staticCardUtil.when(() -> CardUtil.toNotation(2, 1)).thenReturn("Yy");
            staticCardUtil.when(() -> CardUtil.toNotation(5, 2)).thenReturn("Zz");

            // Act
            final String handNotation = HandUtil.toNotation(0b10000000000000001000000000000001L);

            // Assert
            assertEquals("XxYyZz", handNotation);
        }
    }

    @Test
    public void toNotation_whenCardToNotationThrows_rethrowNotationException() {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            staticCardUtil.when(() -> CardUtil.toNotation(0, 0)).thenThrow(new NotationException());

            // Act
            NotationException notationException = assertThrows(NotationException.class, () -> HandUtil.toNotation(0b10000000000000001000000000000001L));

            // Assert
            assertNotNull(notationException);
        }
    }

    @Test
    public void toNotation_2() throws NotationException, BitmaskException {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            staticCardUtil.when(() -> CardUtil.toNotation(0, 0)).thenReturn("Xx");
            staticCardUtil.when(() -> CardUtil.toNotation(2, 1)).thenReturn("Yy");
            staticCardUtil.when(() -> CardUtil.toNotation(5, 2)).thenReturn("Zz");
            final IHand hand = mock(Hand.class);
            when(hand.getBitmask()).thenReturn(0b10000000000000001000000000000001L);

            // Act
            final String handNotation = HandUtil.toNotation(hand);

            // Assert
            assertEquals("XxYyZz", handNotation);
        }
    }

    @Test
    public void toText() throws TextException {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            staticCardUtil.when(() -> CardUtil.toText(0, 0)).thenReturn("X of x");
            staticCardUtil.when(() -> CardUtil.toText(2, 1)).thenReturn("Y of y");
            staticCardUtil.when(() -> CardUtil.toText(5, 2)).thenReturn("Z of z");

            // Act
            final String handNotation = HandUtil.toText(0b10000000000000001000000000000001L);

            // Assert
            assertEquals("X of x, Y of y, Z of z", handNotation);
        }
    }

    @Test
    public void toText_whenCardToTextThrows_rethrowTextException() {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            staticCardUtil.when(() -> CardUtil.toText(0, 0)).thenThrow(new TextException());

            // Act
            final TextException textException = assertThrows(TextException.class, () -> HandUtil.toText(0b10000000000000001000000000000001L));

            // Assert
            assertNotNull(textException);
        }
    }

    @Test
    public void toText_2() throws TextException, BitmaskException {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            staticCardUtil.when(() -> CardUtil.toText(0, 0)).thenReturn("X of x");
            staticCardUtil.when(() -> CardUtil.toText(2, 1)).thenReturn("Y of y");
            staticCardUtil.when(() -> CardUtil.toText(5, 2)).thenReturn("Z of z");
            final IHand hand = mock(Hand.class);
            when(hand.getBitmask()).thenReturn(0b10000000000000001000000000000001L);

            // Act
            final String handText = HandUtil.toText(hand);

            // Assert
            assertEquals("X of x, Y of y, Z of z", handText);
        }
    }
}
