package com.antwika.game.event;

import com.antwika.game.Game;
import com.antwika.game.player.Player;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlayerActionResponse implements IEvent {
    public enum Type { FOLD, CHECK, CALL, BET, RAISE }
    public Player player;
    public Game game;
    public Type action;
    public int amount;
}
