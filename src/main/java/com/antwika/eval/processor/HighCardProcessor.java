package com.antwika.eval.processor;

import com.antwika.eval.data.Evaluation;
import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.eval.util.HandProcessorDataUtil;

import java.util.Arrays;

public class HighCardProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 5;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (data.getHand() == 0L) return;
        if (rankCount > 0 && data.getHighCardKickerCount() < 5) {
            HandProcessorDataUtil.setKicker(data, kickerOffset + data.getHighCardKickerCount(), rankIndex);
            HandProcessorDataUtil.setHighCardKickerCount(data, data.getHighCardKickerCount() + 1);
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (handData.getHand() == 0L) return null;

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.HIGH_CARD)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(handData.getHighCardKickerCount())
                .build();
    }
}
