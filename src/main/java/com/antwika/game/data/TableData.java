package com.antwika.game.data;

import com.antwika.game.actor.Dealer;
import com.antwika.game.core.IPot;
import com.antwika.game.core.ISeat;
import com.antwika.game.core.ITableData;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder(toBuilder=true)
public class TableData implements ITableData {
    private Dealer dealer;
    private List<ISeat> seats;
    private int smallBlind;
    private int bigBlind;
    private List<IPot> pots;
    private int buttonAt;
    private int actionAt;
}
