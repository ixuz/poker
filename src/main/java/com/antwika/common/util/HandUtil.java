package com.antwika.common.util;

import com.antwika.common.data.Hand;
import com.antwika.common.core.ICard;
import com.antwika.common.core.IHand;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;

public class HandUtil {
    public static IHand fromNotation(String notation) throws NotationException {
        try {
            long hand = 0L;
            for (int i = 0; i < notation.length(); i += 2) {
                final ICard card = CardUtil.fromNotation(notation.substring(i, i + 2));
                hand |= CardUtil.toBitmask(card);
            }
            return Hand.builder().bitmask(hand).build();
        } catch (BitmaskException e) {
            throw new NotationException();
        }
    }

    public static String toNotation(long bitmask) throws NotationException {
        final StringBuilder builder = new StringBuilder();
        final String bitmaskString = Long.toBinaryString(bitmask);
        for (int i = bitmaskString.length() - 1; i >= 0; i--) {
            int bitIndex = bitmaskString.length() - (i + 1);
            if (bitmaskString.charAt(i) == '1') {
                try {
                    builder.append(CardUtil.toNotation(bitIndex % 13, bitIndex / 13));
                } catch (NotationException e) {
                    throw new NotationException();
                }
            }
        }
        return builder.toString();
    }

    public static String toNotation(IHand hand) throws NotationException {
        return toNotation(hand.getBitmask());
    }

    public static String toText(long bitmask) throws TextException {
        final StringBuilder builder = new StringBuilder();
        final String bitmaskString = Long.toBinaryString(bitmask);
        for (int i = bitmaskString.length() - 1; i >= 0; i--) {
            int bitIndex = bitmaskString.length() - (i + 1);
            if (bitmaskString.charAt(i) == '1') {
                if (!builder.isEmpty()) builder.append(", ");
                try {
                    builder.append(CardUtil.toText(bitIndex % 13, bitIndex / 13));
                } catch (TextException e) {
                    throw new TextException();
                }
            }
        }
        return builder.toString();
    }

    public static String toText(IHand hand) throws TextException {
        return toText(hand.getBitmask());
    }
}
