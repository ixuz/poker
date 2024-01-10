package com.antwika.eval.processor;

import com.antwika.eval.data.Evaluation;
import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.eval.util.HandDataUtil;
import com.antwika.eval.util.HandProcessorDataUtil;

import java.util.Arrays;

import static com.antwika.common.util.BitmaskUtil.FULL_HOUSE;

public class FullHouseProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 2;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (HandProcessorDataUtil.isHandType(data, BitmaskUtil.FULL_HOUSE)) return;

        if (!HandProcessorDataUtil.hasKicker(data, kickerOffset) && rankCount == 3) {
            HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
        }
        if (!HandProcessorDataUtil.hasKicker(data, kickerOffset + 1) && HandProcessorDataUtil.isNotKicker(data, kickerOffset, rankIndex) && rankCount >= 2) {
            HandProcessorDataUtil.setKicker(data, kickerOffset + 1, rankIndex);
        }
        if (HandProcessorDataUtil.hasKicker(data, kickerOffset) && HandProcessorDataUtil.hasKicker(data, kickerOffset + 1)) {
            HandProcessorDataUtil.configureHandType(data, BitmaskUtil.FULL_HOUSE, true);
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, FULL_HOUSE)) return null;

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.FULL_HOUSE)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(2)
                .build();
    }
}
