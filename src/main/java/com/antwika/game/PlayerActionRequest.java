package com.antwika.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerActionRequest extends Event {
    public Player player;
    public Game game;
    public int totalBet;
    public int toCall;
    public int minBet;
    public int minRaise;
}
