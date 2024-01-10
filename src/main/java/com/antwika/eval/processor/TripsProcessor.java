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

import static com.antwika.common.util.BitmaskUtil.TRIPS;

public class TripsProcessor implements IHandProcessor {
    @Override
    public int maxKickerCount() {
        return 3;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        if (!HandProcessorDataUtil.isHandType(data, BitmaskUtil.TRIPS) || !HandProcessorDataUtil.hasKicker(data, kickerOffset + 2)) {
            if (!HandProcessorDataUtil.hasKicker(data, kickerOffset) && rankCount == 3) {
                HandProcessorDataUtil.setKicker(data, kickerOffset, rankIndex);
                HandProcessorDataUtil.configureHandType(data, BitmaskUtil.TRIPS, true);
            } else if (!HandProcessorDataUtil.hasKicker(data, kickerOffset + 1) && HandProcessorDataUtil.isNotKicker(data, kickerOffset, rankIndex) && rankCount > 0) {
                HandProcessorDataUtil.setKicker(data, kickerOffset + 1, rankIndex);
            } else if (!HandProcessorDataUtil.hasKicker(data, kickerOffset + 2) && HandProcessorDataUtil.isNotKicker(data, kickerOffset + 1, rankIndex) && rankCount > 0) {
                HandProcessorDataUtil.setKicker(data, kickerOffset + 2, rankIndex);
            }
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        if (!HandDataUtil.isHandType(handData, TRIPS)) return null;

        int kickerCount = 0;

        for (int i = 0; i < maxKickerCount(); i += 1) {
            if (handData.getKickers()[kickerOffset + i] != -1) {
                kickerCount += 1;
            }
        }

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.TRIPS)
                .kickers(Arrays.copyOfRange(handData.getKickers(), kickerOffset, kickerOffset + maxKickerCount()))
                .kickersCount(kickerCount)
                .build();
    }
}
