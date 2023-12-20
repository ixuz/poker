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

public class StraightFlushProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 1;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (HandProcessorDataUtil.isHandType(data, BitmaskUtil.STRAIGHT_FLUSH)) return;
        if (rankIndex < FIVE_INDEX) return;

        for (int familyOffset = 0; familyOffset < 52; familyOffset += 13) {
            if (((data.getHand() & straightFlushBitmaskByRankAndFamily[familyOffset + rankIndex]) == straightFlushBitmaskByRankAndFamily[familyOffset + rankIndex])) {
                HandProcessorDataUtil.configureHandType(data, BitmaskUtil.STRAIGHT_FLUSH, true);
                HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
                break;
            }
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, STRAIGHT_FLUSH)) return null;

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.STRAIGHT_FLUSH)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(1)
                .build();
    }
}
