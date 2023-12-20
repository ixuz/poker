package com.antwika.common.data;

import com.antwika.common.core.IRank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Rank implements IRank {
    private int value;
}
