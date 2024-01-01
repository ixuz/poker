package com.antwika.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerActionResponse extends Event {
    public enum Type { FOLD, CHECK, CALL, BET, RAISE }
    public Player player;
    public Game game;
    public Type action;
    public int amount;
}
