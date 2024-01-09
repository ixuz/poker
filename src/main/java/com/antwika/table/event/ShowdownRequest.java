package com.antwika.table.event;

import com.antwika.table.data.TableData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShowdownRequest implements IEvent {
    private TableData tableData;
}
