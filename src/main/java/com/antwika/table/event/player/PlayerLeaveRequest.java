package com.antwika.table.event.player;

import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.event.IEvent;
import com.antwika.table.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerLeaveRequest implements IEvent {
    private TableData tableData;
    private SeatData seatData;
    private Player player;
}
