package com.antwika.game.data;

import com.antwika.game.player.Player;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class SeatData {
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
