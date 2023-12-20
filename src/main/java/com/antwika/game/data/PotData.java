package com.antwika.game.data;

import com.antwika.game.core.IActor;
import com.antwika.game.core.IPot;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder(toBuilder=true)
public class PotData implements IPot {
    private List<IActor> actors;
    private int amount;
}
