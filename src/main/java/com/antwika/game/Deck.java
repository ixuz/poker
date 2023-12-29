package com.antwika.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Long> cards = new ArrayList<>();

    public void resetAndShuffle() {
        cards.clear();
        for (int i = 0; i < 52; i += 1) {
            cards.add(1L << i);
        }
        Collections.shuffle(cards);
    }

    public Long draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }
}
