package com.antwika.common.util;

import com.antwika.common.core.ICard;
import com.antwika.common.core.IRank;
import com.antwika.common.core.ISuit;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;
import com.antwika.common.util.CardUtil;
import com.antwika.common.util.RankUtil;
import com.antwika.common.util.SuitUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static com.antwika.common.util.BitmaskUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardUtilTest {

    @Test
    @Tag("IntegrationTest")
    public void toBitmask() throws BitmaskException {
        // Arrange
        final IRank rank = mock(IRank.class);
        final ISuit suit = mock(ISuit.class);

        when(rank.getValue()).thenReturn(THREE_INDEX);
        when(suit.getValue()).thenReturn(DIAMONDS_INDEX);

        // Act
        assertEquals(0b0000000000000000000000000000000000000100000000000000L, CardUtil.toBitmask(rank, suit));
    }

    @Test
    public void toNotation() throws NotationException {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toNotation(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toNotation(2)).thenReturn("x");

            // Act
            final String cardNotation = CardUtil.toNotation(1, 2);

            // Assert
            assertEquals("Xx", cardNotation);
        }
    }

    @Test
    public void toNotation_whenRankToNotationThrows_rethrowNotationException() {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toNotation(1)).thenThrow(new NotationException());
            staticSuitUtil.when(() -> SuitUtil.toNotation(2)).thenReturn("x");

            // Act
            final NotationException notationException = assertThrows(NotationException.class, () -> CardUtil.toNotation(1, 2));

            // Assert
            assertNotNull(notationException);
        }
    }

    @Test
    public void toNotation_whenSuitToNotationThrows_rethrowNotationException() {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toNotation(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toNotation(2)).thenThrow(new NotationException());

            // Act
            final NotationException notationException = assertThrows(NotationException.class, () -> CardUtil.toNotation(1, 2));

            // Assert
            assertNotNull(notationException);
        }
    }

    @Test
    public void toNotation_2() throws NotationException {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toNotation(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toNotation(2)).thenReturn("x");
            final IRank rank = mock(IRank.class);
            final ISuit suit = mock(ISuit.class);
            when(rank.getValue()).thenReturn(1);
            when(suit.getValue()).thenReturn(2);

            // Act
            final String cardNotation = CardUtil.toNotation(rank, suit);

            // Assert
            assertEquals("Xx", cardNotation);
        }
    }

    @Test
    public void toNotation_3() throws NotationException {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            final IRank rank = mock(IRank.class);
            final ISuit suit = mock(ISuit.class);
            final ICard card = mock(ICard.class);
            staticRankUtil.when(() -> RankUtil.toNotation(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toNotation(2)).thenReturn("y");
            when(rank.getValue()).thenReturn(1);
            when(suit.getValue()).thenReturn(2);
            when(card.getRank()).thenReturn(rank);
            when(card.getSuit()).thenReturn(suit);

            // Act
            final String cardText = CardUtil.toNotation(card);

            // Assert
            assertEquals("Xy", cardText);
        }
    }

    @Test
    public void toText() throws TextException {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toText(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toText(2)).thenReturn("Y");

            // Act
            final String cardText = CardUtil.toText(1, 2);

            // Assert
            assertEquals("X of Y", cardText);
        }
    }

    @Test
    public void toText_whenRankToTextThrows_rethrowTextException() {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toText(1)).thenThrow(new TextException());
            staticSuitUtil.when(() -> SuitUtil.toText(2)).thenReturn("x");

            // Act
            final TextException textException = assertThrows(TextException.class, () -> CardUtil.toText(1, 2));

            // Assert
            assertNotNull(textException);
        }
    }

    @Test
    public void toText_whenSuitToTextThrows_rethrowTextException() {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toText(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toText(2)).thenThrow(new TextException());

            // Act
            final TextException textException = assertThrows(TextException.class, () -> CardUtil.toText(1, 2));

            // Assert
            assertNotNull(textException);
        }
    }

    @Test
    public void toText_2() throws TextException {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            staticRankUtil.when(() -> RankUtil.toText(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toText(2)).thenReturn("Y");
            final IRank rank = mock(IRank.class);
            final ISuit suit = mock(ISuit.class);
            when(rank.getValue()).thenReturn(1);
            when(suit.getValue()).thenReturn(2);

            // Act
            final String cardText = CardUtil.toText(rank, suit);

            // Assert
            assertEquals("X of Y", cardText);
        }
    }

    @Test
    public void toText_3() throws TextException {
        try (MockedStatic<RankUtil> staticRankUtil = mockStatic(RankUtil.class);
             MockedStatic<SuitUtil> staticSuitUtil = mockStatic(SuitUtil.class)) {
            // Arrange
            final IRank rank = mock(IRank.class);
            final ISuit suit = mock(ISuit.class);
            final ICard card = mock(ICard.class);
            staticRankUtil.when(() -> RankUtil.toText(1)).thenReturn("X");
            staticSuitUtil.when(() -> SuitUtil.toText(2)).thenReturn("Y");
            when(rank.getValue()).thenReturn(1);
            when(suit.getValue()).thenReturn(2);
            when(card.getRank()).thenReturn(rank);
            when(card.getSuit()).thenReturn(suit);

            // Act
            final String cardText = CardUtil.toText(card);

            // Assert
            assertEquals("X of Y", cardText);
        }
    }
}
