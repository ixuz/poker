package com.antwika.game.event;

import com.antwika.game.data.GameData;
import com.antwika.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShowdownEvent implements IEvent {
    private GameData gameData;
}
