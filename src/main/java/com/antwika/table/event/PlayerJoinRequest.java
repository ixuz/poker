package com.antwika.table.event;

import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerJoinRequest implements IEvent {
    private TableData tableData;
    private SeatData seatData;
    private Player player;
    private int amount;
}
