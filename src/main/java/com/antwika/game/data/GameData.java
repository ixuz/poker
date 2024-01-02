package com.antwika.game.data;

import com.antwika.game.common.Prng;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GameData {
    private final String tableName;
    private String gameType;
    private List<Seat> seats;
    private final List<Pot> pots = new ArrayList<>();
    private final DeckData deckData;
    private final int smallBlind;
    private final int bigBlind;
    private long handId;
    private int buttonAt;
    private int actionAt;
    private int totalBet;
    private int lastRaise;
    private Long cards;
    private int delivered;
    private int chipsInPlay;
    private final Prng prng;
}
