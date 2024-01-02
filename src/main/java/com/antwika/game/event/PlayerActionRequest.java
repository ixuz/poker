package com.antwika.game.event;

import com.antwika.game.data.GameData;
import com.antwika.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerActionRequest implements IEvent {
    private Player player;
    private GameData gameData;
    private int totalBet;
    private int toCall;
    private int minBet;
    private int minRaise;
}
