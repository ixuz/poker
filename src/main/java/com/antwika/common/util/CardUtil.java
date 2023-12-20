package com.antwika.common.util;

import com.antwika.common.data.Card;
import com.antwika.common.core.ICard;
import com.antwika.common.core.IRank;
import com.antwika.common.core.ISuit;
import com.antwika.common.exception.BitmaskException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.exception.TextException;

import static com.antwika.common.util.BitmaskUtil.RANK_TO_ANY_OF_RANK;

public class CardUtil {
    public static long toBitmask(IRank rank, ISuit suit) throws BitmaskException {
        try {
            return RANK_TO_ANY_OF_RANK[rank.getValue()] & SuitUtil.toBitmask(suit.getValue());
        } catch (Exception exception) {
            throw new BitmaskException();
        }
    }

    public static long toBitmask(ICard card) throws BitmaskException {
        return toBitmask(card.getRank(), card.getSuit());
    }

    public static ICard fromNotation(String notation) {
        final IRank rank = RankUtil.fromNotation(String.valueOf(notation.charAt(0)));
        final ISuit suit = SuitUtil.fromNotation(String.valueOf(notation.charAt(1)));
        return Card.builder().rank(rank).suit(suit).build();
    }

    public static String toNotation(int rank, int suit) throws NotationException {
        try {
            return String.format("%s%s", RankUtil.toNotation(rank), SuitUtil.toNotation(suit));
        } catch (NotationException exception) {
            throw new NotationException();
        }
    }

    public static String toNotation(IRank rank, ISuit suit) throws NotationException {
        return String.format("%s", toNotation(rank.getValue(), suit.getValue()));
    }

    public static String toNotation(ICard card) throws NotationException {
        return String.format("%s", toNotation(card.getRank(), card.getSuit()));
    }

    public static String toText(int rank, int suit) throws TextException {
        try {
            return String.format("%s of %s", RankUtil.toText(rank), SuitUtil.toText(suit));
        } catch (TextException e) {
            throw new TextException();
        }
    }

    public static String toText(IRank rank, ISuit suit) throws TextException {
        return String.format("%s", toText(rank.getValue(), suit.getValue()));
    }

    public static String toText(ICard card) throws TextException {
        return String.format("%s", toText(card.getRank(), card.getSuit()));
    }
}
