package com.antwika.eval.data;

import com.antwika.eval.core.IHandProcessorData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class HandProcessorData implements IHandProcessorData {
    private long hand;
    private int[] kickers;
    private long handType;
    private int highCardKickerCount;
}
