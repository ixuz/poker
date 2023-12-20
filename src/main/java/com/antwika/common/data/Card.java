package com.antwika.common.data;

import com.antwika.common.core.ICard;
import com.antwika.common.core.IRank;
import com.antwika.common.core.ISuit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Card implements ICard {
    private IRank rank;
    private ISuit suit;
}
