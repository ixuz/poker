package com.antwika.eval.util;

import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.data.HandData;

public class HandProcessorUtil {
    public static IHandData toHandData(IHandProcessorData data) {
        return HandData.builder()
                .hand(data.getHand())
                .kickers(data.getKickers())
                .handType(data.getHandType())
                .highCardKickerCount(data.getHighCardKickerCount())
                .build();
    }
}
