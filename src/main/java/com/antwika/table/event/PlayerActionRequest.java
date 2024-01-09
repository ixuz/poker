package com.antwika.table.event;

import com.antwika.table.data.TableData;
import com.antwika.table.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerActionRequest implements IEvent {
    private Player player;
    private TableData tableData;
    private int totalBet;
    private int toCall;
    private int minBet;
    private int minRaise;
}
