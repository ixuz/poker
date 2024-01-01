package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;

import java.util.ArrayList;
import java.util.List;

public class GameUtil {
    public static String toNotation(long cards) throws NotationException {
        final String cardsNotation = HandUtil.toNotation(cards);

        final List<String> cn = new ArrayList<>();
        for (int i = 0; i < cardsNotation.length(); i += 2) {
            final String cardNotation = cardsNotation.substring(i, i+2);
            cn.add(cardNotation);
        }

        return String.join(" ", cn);
    }
}
