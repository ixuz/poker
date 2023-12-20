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

import static com.antwika.common.util.BitmaskUtil.TWO_PAIR;

public class TwoPairProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 3;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (HandProcessorDataUtil.isHandType(data, BitmaskUtil.TWO_PAIR) && HandProcessorDataUtil.hasKicker(data, kickerOffset + 2)) return;
        if (rankCount <= 0) return;

        if (!HandProcessorDataUtil.hasKicker(data, kickerOffset) && rankCount == 2) {
            HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
        } else if (!HandProcessorDataUtil.hasKicker(data, kickerOffset + 1) && rankCount == 2) {
            HandProcessorDataUtil.setKicker(data, kickerOffset + 1, rankIndex);
            HandProcessorDataUtil.configureHandType(data, BitmaskUtil.TWO_PAIR, true);
        } else if (!HandProcessorDataUtil.hasKicker(data, kickerOffset + 2)) {
            HandProcessorDataUtil.setKicker(data, kickerOffset + 2, rankIndex);
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, TWO_PAIR)) return null;

        int kickerCount = 0;

        for (int i = 0; i < maxKickerCount(); i += 1) {
            if (handData.getKickers()[kickerOffset + i] != -1) {
                kickerCount += 1;
            }
        }

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.TWO_PAIR)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(kickerCount)
                .build();
    }
}
