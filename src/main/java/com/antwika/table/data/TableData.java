package com.antwika.table.data;

import com.antwika.table.common.Prng;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TableData {
    public enum GameStage { NONE, HAND_BEGUN, PREFLOP, FLOP, TURN, RIVER, SHOWDOWN }
    private final String tableName;
    private String gameType;
    private GameStage gameStage;
    private List<SeatData> seats;
    private final List<PotData> pots = new ArrayList<>();
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
