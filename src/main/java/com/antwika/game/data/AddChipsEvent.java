package com.antwika.game.data;

import com.antwika.game.World;
import com.antwika.game.core.IActor;
import com.antwika.game.core.IEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class AddChipsEvent implements IEvent {
    private final World world;
    private final String type = "ADD_CHIPS";
    private final IActor actor;
    private final int seatIndex;
    private final int amount;
}
