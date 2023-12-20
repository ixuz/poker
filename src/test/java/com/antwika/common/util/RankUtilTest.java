package com.antwika.common.util;

import com.antwika.common.core.IRank;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;
import com.antwika.common.util.RankUtil;
import org.junit.jupiter.api.Test;

import static com.antwika.common.util.BitmaskUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RankUtilTest {
    @Test
    public void toBitmask() throws BitmaskException {
        assertEquals(TWO, RankUtil.toBitmask(0));
        assertEquals(THREE, RankUtil.toBitmask(1));
        assertEquals(FOUR, RankUtil.toBitmask(2));
        assertEquals(FIVE, RankUtil.toBitmask(3));
        assertEquals(SIX, RankUtil.toBitmask(4));
        assertEquals(SEVEN, RankUtil.toBitmask(5));
        assertEquals(EIGHT, RankUtil.toBitmask(6));
        assertEquals(NINE, RankUtil.toBitmask(7));
        assertEquals(TEN, RankUtil.toBitmask(8));
        assertEquals(JACK, RankUtil.toBitmask(9));
        assertEquals(QUEEN, RankUtil.toBitmask(10));
        assertEquals(KING, RankUtil.toBitmask(11));
        assertEquals(ACE, RankUtil.toBitmask(12));
        assertThrows(BitmaskException.class, () -> RankUtil.toBitmask(-1));
    }

    @Test
    public void toNotation() throws NotationException {
        assertEquals("2", RankUtil.toNotation(0));
        assertEquals("3", RankUtil.toNotation(1));
        assertEquals("4", RankUtil.toNotation(2));
        assertEquals("5", RankUtil.toNotation(3));
        assertEquals("6", RankUtil.toNotation(4));
        assertEquals("7", RankUtil.toNotation(5));
        assertEquals("8", RankUtil.toNotation(6));
        assertEquals("9", RankUtil.toNotation(7));
        assertEquals("T", RankUtil.toNotation(8));
        assertEquals("J", RankUtil.toNotation(9));
        assertEquals("Q", RankUtil.toNotation(10));
        assertEquals("K", RankUtil.toNotation(11));
        assertEquals("A", RankUtil.toNotation(12));
        assertThrows(NotationException.class, () -> RankUtil.toNotation(-1));
    }

    @Test
    public void toNotation_2() throws NotationException {
        // Arrange
        final IRank rank = mock(IRank.class);
        when(rank.getValue()).thenReturn(1);

        // Act
        final String rankNotation = RankUtil.toNotation(rank);

        // Assert
        assertEquals("3", rankNotation);
    }

    @Test
    public void toText() throws TextException {
        assertEquals("Two", RankUtil.toText(0));
        assertEquals("Three", RankUtil.toText(1));
        assertEquals("Four", RankUtil.toText(2));
        assertEquals("Five", RankUtil.toText(3));
        assertEquals("Six", RankUtil.toText(4));
        assertEquals("Seven", RankUtil.toText(5));
        assertEquals("Eight", RankUtil.toText(6));
        assertEquals("Nine", RankUtil.toText(7));
        assertEquals("Ten", RankUtil.toText(8));
        assertEquals("Jack", RankUtil.toText(9));
        assertEquals("Queen", RankUtil.toText(10));
        assertEquals("King", RankUtil.toText(11));
        assertEquals("Ace", RankUtil.toText(12));
        assertThrows(TextException.class, () -> RankUtil.toText(-1));
    }

    @Test
    public void toText_2() throws TextException {
        // Arrange
        final IRank rank = mock(IRank.class);
        when(rank.getValue()).thenReturn(1);

        // Act
        final String rankText = RankUtil.toText(rank);

        // Assert
        assertEquals("Three", rankText);
    }
}
