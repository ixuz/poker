package com.antwika.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerActionResponse extends Event {
    public Player player;
    public Game game;
    public String action;
    public int amount;
}
