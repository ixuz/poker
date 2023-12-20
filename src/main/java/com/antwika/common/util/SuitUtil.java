package com.antwika.common.util;

import com.antwika.common.data.Suit;
import com.antwika.common.core.ISuit;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;

import java.util.Arrays;

import static com.antwika.common.util.BitmaskUtil.*;

public class SuitUtil {
    private static final String[] SUIT_TO_NOTATION = new String[]{ "c", "d", "h", "s" };
    private static final String[] SUIT_TO_TEXT = new String[]{ "Clubs", "Diamonds", "Hearts", "Spades" };
    private static final long[] SUIT_TO_BITMASK = new long[]{ CLUBS, DIAMONDS, HEARTS, SPADES };

    public static ISuit fromNotation(String notation) {
        return Suit.builder()
                .value(Arrays.asList(SUIT_TO_NOTATION).indexOf(notation))
                .build();
    }

    public static long toBitmask(int value) throws BitmaskException {
        try {
            return SUIT_TO_BITMASK[value];
        } catch (Exception e) {
            throw new BitmaskException();
        }
    }

    public static String toNotation(int value) throws NotationException {
        try {
            return String.format("%s", SUIT_TO_NOTATION[value]);
        } catch (Exception e) {
            throw new NotationException();
        }
    }

    public static String toNotation(ISuit suit) throws NotationException {
        return String.format("%s", toNotation(suit.getValue()));
    }

    public static String toText(int value) throws TextException {
        try {
            return String.format("%s", SUIT_TO_TEXT[value]);
        } catch (Exception e) {
            throw new TextException();
        }
    }

    public static String toText(ISuit suit) throws TextException {
        return String.format("%s", toText(suit.getValue()));
    }
}
