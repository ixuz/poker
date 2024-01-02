package com.antwika.game.event;

import com.antwika.game.data.GameData;
import com.antwika.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerActionResponse implements IEvent {
    public enum Type { FOLD, CHECK, CALL, BET, RAISE }
    public Player player;
    public GameData gameData;
    public Type action;
    public int amount;
}
