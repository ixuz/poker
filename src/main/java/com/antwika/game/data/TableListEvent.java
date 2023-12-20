package com.antwika.game.data;

import com.antwika.game.World;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.ITableData;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@ToString
public class TableListEvent implements IEvent {
    private final World world;
    private final String type = "TABLE_LIST";
    private final IActor actor;
    private final List<ITableData> tables;
}
