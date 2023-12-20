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

import static com.antwika.common.util.BitmaskUtil.PAIR;

public class PairProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 4;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {

        final boolean kicker0 = HandProcessorDataUtil.hasKicker(data, kickerOffset);
        final boolean kicker1 = HandProcessorDataUtil.hasKicker(data, kickerOffset + 1);
        final boolean kicker2 = HandProcessorDataUtil.hasKicker(data, kickerOffset + 2);
        final boolean kicker3 = HandProcessorDataUtil.hasKicker(data, kickerOffset + 3);
        final boolean foundAllKickers = kicker0 && kicker1 && kicker2 && kicker3;

        if (!HandProcessorDataUtil.isHandType(data, BitmaskUtil.PAIR) || !foundAllKickers) {
            if (!kicker0 && rankCount == 2) {
                HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
                HandProcessorDataUtil.configureHandType(data, BitmaskUtil.PAIR, true);
            } else if (!kicker1 && rankCount > 0) {
                HandProcessorDataUtil.setKicker(data, kickerOffset + 1, rankIndex);
            } else if (!kicker2 && rankCount > 0) {
                HandProcessorDataUtil.setKicker(data, kickerOffset + 2, rankIndex);
            } else if (!kicker3 && rankCount > 0) {
                HandProcessorDataUtil.setKicker(data, kickerOffset + 3, rankIndex);
            }
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, PAIR)) return null;

        int kickerCount = 0;

        for (int i = 0; i < maxKickerCount(); i += 1) {
            if (handData.getKickers()[kickerOffset + i] != -1) {
                kickerCount += 1;
            }
        }

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.PAIR)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(kickerCount)
                .build();
    }
}
