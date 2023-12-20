package com.antwika.common.util;

import com.antwika.common.core.ISuit;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;
import com.antwika.common.util.SuitUtil;
import org.junit.jupiter.api.Test;

import static com.antwika.common.util.BitmaskUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuitUtilTest {
    @Test
    public void toBitmask() throws BitmaskException {
        assertEquals(CLUBS, SuitUtil.toBitmask(0));
        assertEquals(DIAMONDS, SuitUtil.toBitmask(1));
        assertEquals(HEARTS, SuitUtil.toBitmask(2));
        assertEquals(SPADES, SuitUtil.toBitmask(3));
        assertThrows(BitmaskException.class, () -> SuitUtil.toBitmask(-1));
    }

    @Test
    public void toNotation() throws NotationException {
        assertEquals("c", SuitUtil.toNotation(0));
        assertEquals("d", SuitUtil.toNotation(1));
        assertEquals("h", SuitUtil.toNotation(2));
        assertEquals("s", SuitUtil.toNotation(3));
        assertThrows(NotationException.class, () -> SuitUtil.toNotation(-1));
    }

    @Test
    public void toNotation_2() throws NotationException {
        // Arrange
        final ISuit suit = mock(ISuit.class);
        when(suit.getValue()).thenReturn(1);

        // Act
        final String suitNotation = SuitUtil.toNotation(suit);

        // Assert
        assertEquals("d", suitNotation);
    }

    @Test
    public void toText() throws TextException {
        assertEquals("Clubs", SuitUtil.toText(0));
        assertEquals("Diamonds", SuitUtil.toText(1));
        assertEquals("Hearts", SuitUtil.toText(2));
        assertEquals("Spades", SuitUtil.toText(3));
        assertThrows(TextException.class, () -> SuitUtil.toText(-1));
    }

    @Test
    public void toText_2() throws TextException {
        // Arrange
        final ISuit suit = mock(ISuit.class);
        when(suit.getValue()).thenReturn(1);

        // Act
        final String suitText = SuitUtil.toText(suit);

        // Assert
        assertEquals("Diamonds", suitText);
    }
}
