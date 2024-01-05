package com.antwika.game.event;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerLeaveEvent implements IEvent {
    private GameData gameData;
    private SeatData seatData;
    private Player player;
}
