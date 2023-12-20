package com.antwika.common.util;

import com.antwika.common.data.Rank;
import com.antwika.common.core.IRank;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;

import java.util.Arrays;

import static com.antwika.common.util.BitmaskUtil.*;

public class RankUtil {
    private static final long[] RANK_TO_BITMASK = new long[]{ TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE };
    private static final String[] RANK_TO_NOTATION = new String[]{ "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A" };
    private static final String[] RANK_TO_TEXT = new String[]{ "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace" };

    public static IRank fromNotation(String notation) {
        return Rank.builder()
                .value(Arrays.asList(RANK_TO_NOTATION).indexOf(notation))
                .build();
    }

    public static long toBitmask(int value) throws BitmaskException {
        try {
            return RANK_TO_BITMASK[value];
        } catch (Exception e) {
            throw new BitmaskException();
        }
    }

    public static String toNotation(int rank) throws NotationException {
        try {
            return String.format("%s", RANK_TO_NOTATION[rank]);
        } catch (Exception e) {
            throw new NotationException();
        }
    }

    public static String toNotation(IRank rank) throws NotationException {
        return String.format("%s", toNotation(rank.getValue()));
    }

    public static String toText(int rank) throws TextException {
        try {
            return String.format("%s", RANK_TO_TEXT[rank]);
        } catch (Exception e) {
            throw new TextException();
        }
    }

    public static String toText(IRank rank) throws TextException {
        return String.format("%s", toText(rank.getValue()));
    }
}
