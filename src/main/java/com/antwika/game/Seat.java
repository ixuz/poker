package com.antwika.game;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class Seat {
    @ToString.Include
    private Player player;
    private int seatIndex;
    private int stack;
    private boolean hasActed;
    private boolean hasFolded;
    private int committed;
    private int totalCommitted;
    private Long cards;
    private boolean postedSmallBlindLastRound;
    private boolean postedBigBlindLastRound;
}
