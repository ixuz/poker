package com.antwika.table.event.orbit;

import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrbitEndRequest implements IEvent {
    private TableData tableData;
}
