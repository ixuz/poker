package com.antwika.table.event.player;

import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerActionResponse implements IEvent {
    public enum Type { FOLD, CHECK, CALL, BET, RAISE }
    public Player player;
    public TableData tableData;
    public Type action;
    public int amount;
}
