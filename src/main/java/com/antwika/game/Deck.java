package com.antwika.game;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Deck {
    private static final Logger logger = LoggerFactory.getLogger(Deck.class);

    private final List<Long> cards = new ArrayList<>();

    private final Prng prng;

    public Deck(long prngSeed) {
        this.prng = new Prng(prngSeed);
    }

    public static void resetAndShuffle(Deck deck) {
        final List<Long> cards = deck.getCards();
        cards.clear();
        for (int i = 0; i < 52; i += 1) {
            cards.add(1L << i);
        }
        Collections.shuffle(cards, deck.getPrng().getRandomInstance());
        logger.debug("Deck shuffled");
    }

    public static Long draw(Deck deck) {
        final List<Long> cards = deck.getCards();
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }
}
