package com.antwika.game.util;

import com.antwika.game.data.DeckData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DeckUtil {
    private static final Logger logger = LoggerFactory.getLogger(DeckUtil.class);

    public static void resetAndShuffle(DeckData deckData) {
        final List<Long> cards = deckData.getCards();
        cards.clear();
        for (int i = 0; i < 52; i += 1) {
            cards.add(1L << i);
        }
        Collections.shuffle(cards, deckData.getPrng().getRandomInstance());
        logger.debug("Deck shuffled");
    }

    public static Long draw(DeckData deckData) {
        final List<Long> cards = deckData.getCards();
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }
}
