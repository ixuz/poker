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

import static com.antwika.common.util.BitmaskUtil.*;

public class QuadsProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 2;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (!HandProcessorDataUtil.isHandType(data, BitmaskUtil.QUADS)) {
            if (Long.bitCount(data.getHand() & RANK_TO_ANY_OF_RANK[rankIndex]) == 4) {
                HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
            } else if (Long.bitCount(data.getHand() & RANK_TO_ANY_OF_RANK[rankIndex]) > 0) {
                HandProcessorDataUtil.setKicker(data, kickerOffset + 1, rankIndex);
            }

            if (HandProcessorDataUtil.hasKicker(data, kickerOffset) && HandProcessorDataUtil.hasKicker(data, kickerOffset + 1)) {
                HandProcessorDataUtil.configureHandType(data, BitmaskUtil.QUADS, true);
            }
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, QUADS)) return null;

        int kickerCount = 0;

        for (int i = 0; i < maxKickerCount(); i += 1) {
            if (handData.getKickers()[kickerOffset + i] != -1) {
                kickerCount += 1;
            }
        }

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.QUADS)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(kickerCount)
                .build();
    }
}
