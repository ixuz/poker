package com.antwika.game.data;

import com.antwika.game.World;
import com.antwika.game.core.IEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class DummyEvent implements IEvent {
    private final World world;
    private final String type = "DUMMY";
}
