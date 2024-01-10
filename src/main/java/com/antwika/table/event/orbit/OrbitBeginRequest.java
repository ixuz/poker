package com.antwika.table.event.orbit;

import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrbitBeginRequest implements IEvent {
    private TableData tableData;
    private int dealCommunityCardCount;
}
