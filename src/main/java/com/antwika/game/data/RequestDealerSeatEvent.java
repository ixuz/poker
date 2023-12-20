package com.antwika.game.data;

import com.antwika.game.World;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import com.antwika.game.core.ITableData;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class RequestDealerSeatEvent implements IEvent {
    private final World world;
    private final String type = "REQUEST_DEALER_SEAT";
    private final IActor actor;
    private final ITableData tableData;
}
