package com.antwika.eval.data;

import com.antwika.eval.core.IHandData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class HandData implements IHandData {
    private long hand;
    private int[] kickers;
    private long handType;
    private int highCardKickerCount;
}
