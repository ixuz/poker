package com.antwika.common.data;

import com.antwika.common.core.ISuit;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Suit implements ISuit {
    private int value;
}
