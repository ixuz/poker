package com.antwika.game.data;

import com.antwika.game.core.IActor;
import com.antwika.game.core.ISeat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder=true)
public class SeatData implements ISeat {
    private IActor actor;
    private int stack;
    private int committed;
}
