package com.antwika.table.event.hand;

import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DealCommunityCardsRequest implements IEvent {
    private TableData tableData;
    private int count;
}
