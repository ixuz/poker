package com.antwika.common.data;

import com.antwika.common.core.IHand;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Hand implements IHand {
    private long bitmask;
}
