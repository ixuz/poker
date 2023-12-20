package com.antwika.common.data;

import com.antwika.common.core.ICard;
import com.antwika.common.core.IRank;
import com.antwika.common.core.ISuit;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;
import com.antwika.common.util.CardUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        Card.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Card(rank=null, suit=null)", Card.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getRank() {
        final IRank rank = mock(IRank.class);
        assertEquals(rank, Card.builder().rank(rank).build().getRank());
    }

    @Test
    @Tag("UnitTest")
    public void getSuit() {
        final ISuit suit = mock(ISuit.class);
        assertEquals(suit, Card.builder().suit(suit).build().getSuit());
    }

    @Test
    @Tag("UnitTest")
    public void toNotation() throws NotationException {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            final IRank rank = mock(IRank.class);
            final ISuit suit = mock(ISuit.class);
            final ICard card = Card.builder().rank(rank).suit(suit).build();
            staticCardUtil.when(() -> CardUtil.toNotation(card)).thenReturn("Xy");

            // Act
            final String cardText = CardUtil.toNotation(card);

            // Assert
            assertEquals("Xy", cardText);
        }
    }

    @Test
    @Tag("UnitTest")
    public void toText() throws TextException {
        try (MockedStatic<CardUtil> staticCardUtil = mockStatic(CardUtil.class)) {
            // Arrange
            final IRank rank = mock(IRank.class);
            final ISuit suit = mock(ISuit.class);
            final ICard card = Card.builder().rank(rank).suit(suit).build();
            staticCardUtil.when(() -> CardUtil.toText(card)).thenReturn("X of Y");

            // Act
            final String cardText = CardUtil.toText(card);

            // Assert
            assertEquals("X of Y", cardText);
        }
    }
}
