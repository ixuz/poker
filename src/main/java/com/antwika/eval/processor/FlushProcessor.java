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

public class FlushProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 1;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (HandProcessorDataUtil.isHandType(data, BitmaskUtil.FLUSH) || rankIndex < FIVE_INDEX) return;

        long hand = data.getHand();
        long rankMask = RANK_TO_ANY_OF_RANK[rankIndex];
        for (int familyIndex = 0; familyIndex < 4; familyIndex += 1) {
            long familyOffset = FAMILY_OFFSETS[familyIndex];
            long family = FAMILIES[familyIndex];
            int count = Long.bitCount((hand >> familyOffset) & FAMILY);
            if ((count >= 5 && (((hand & family) & rankMask) > 0L))) {
                HandProcessorDataUtil.configureHandType(data, BitmaskUtil.FLUSH, true);
                HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
                break;
            }
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, FLUSH)) return null;

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.FLUSH)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(1)
                .build();
    }
}
