package com.antwika.game;

import com.antwika.game.common.Prng;
import com.antwika.game.data.DeckData;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Deck {
    private static final Logger logger = LoggerFactory.getLogger(Deck.class);

    @Getter
    private final DeckData deckData;

    public Deck(long prngSeed) {
        deckData = new DeckData(prngSeed);
    }

    public static void resetAndShuffle(Deck deck) {
        final List<Long> cards = deck.getDeckData().getCards();
        cards.clear();
        for (int i = 0; i < 52; i += 1) {
            cards.add(1L << i);
        }
        Collections.shuffle(cards, deck.getDeckData().getPrng().getRandomInstance());
        logger.debug("Deck shuffled");
    }

    public static Long draw(Deck deck) {
        final List<Long> cards = deck.getDeckData().getCards();
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }
}
