package com.antwika.table.event;

import com.antwika.table.data.TableData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShowdownEvent implements IEvent {
    private TableData tableData;
}
