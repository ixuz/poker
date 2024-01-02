package com.antwika.game;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GameData {
    private final String tableName;
    private final String gameType = "Hold'em No Limit";
    private List<Seat> seats = new ArrayList<>();
    private final List<Pot> pots = new ArrayList<>();
    private final Deck deck;
    private final int smallBlind;
    private final int bigBlind;
    private long handId = 0L;
    private int buttonAt = 0;
    private int actionAt = 0;
    private int totalBet = 0;
    private int lastRaise = 0;
    private Long cards = 0L;
    private int delivered = 0;
    private int chipsInPlay = 0;
}
