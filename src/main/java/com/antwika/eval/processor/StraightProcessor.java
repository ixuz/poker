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

public class StraightProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 1;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (HandProcessorDataUtil.isHandType(data, STRAIGHT)) return;
        if (rankIndex < FIVE_INDEX) return;

        for (int i = 0; i < 5; i++) {
            int wrappingRankIndex = ((rankIndex - i) == -1) ? ACE_INDEX : (rankIndex - i);
            if (!((data.getHand() & RANK_TO_ANY_OF_RANK[wrappingRankIndex]) > 0L)) break;
            if (i == 4 && !HandProcessorDataUtil.hasKicker(data, kickerOffset)) {
                HandProcessorDataUtil.configureHandType(data, STRAIGHT, true);
                HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
                break;
            }
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, STRAIGHT)) return null;

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.STRAIGHT)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(1)
                .build();
    }
}
