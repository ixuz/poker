package com.antwika.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerActionRequest extends Event {
    private Player player;
    private Game game;
    private int totalBet;
    private int toCall;
    private int minBet;
    private int minRaise;
}
