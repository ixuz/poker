package com.antwika.eval.processor;

import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.data.Evaluation;
import com.antwika.common.util.BitmaskUtil;

import java.util.Arrays;

public class TexasHoldemProcessor implements IHandProcessor  {
    final IHandProcessor[] processors = new IHandProcessor[] {
            new StraightFlushProcessor(),
            new QuadsProcessor(),
            new FullHouseProcessor(),
            new FlushProcessor(),
            new StraightProcessor(),
            new TripsProcessor(),
            new TwoPairProcessor(),
            new PairProcessor(),
            new HighCardProcessor(),
    };
    final int kickersSize = Arrays.stream(processors).mapToInt(IHandProcessor::maxKickerCount).sum();

    @Override
    public int maxKickerCount() {
        return kickersSize;
    }

    @Override
    public void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset) {
        for (int processorKickerOffset = 0, i = 0; i < processors.length; i += 1) {
            final IHandProcessor processor = processors[i];
            processor.process(data, rankIndex, rankCount, processorKickerOffset);
            processorKickerOffset += processor.maxKickerCount();
        }
    }

    @Override
    public IEvaluation toEvaluation(IHandData handData, int kickerOffset) {
        for (int processorKickerOffset = 0, i = 0; i < processors.length; i += 1) {
            final IHandProcessor processor = processors[i];
            final IEvaluation evaluation = processor.toEvaluation(handData, processorKickerOffset);
            if (evaluation != null) {
                return evaluation;
            }
            processorKickerOffset += processor.maxKickerCount();
        }

        return Evaluation.builder()
                .hand(0L)
                .handType(BitmaskUtil.NONE)
                .kickers(new int[]{})
                .kickersCount(0)
                .build();
    }
}
