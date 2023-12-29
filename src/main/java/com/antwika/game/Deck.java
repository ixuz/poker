package com.antwika.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private static final Logger logger = LoggerFactory.getLogger(Deck.class);
    private final List<Long> cards = new ArrayList<>();

    private final Random prng;

    public Deck(long prngSeed) {
        this.prng = new Random(prngSeed);
    }

    public void resetAndShuffle() {
        cards.clear();
        for (int i = 0; i < 52; i += 1) {
            cards.add(1L << i);
        }
        Collections.shuffle(cards, prng);
        logger.debug("Deck shuffled");
    }

    public Long draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }
}
