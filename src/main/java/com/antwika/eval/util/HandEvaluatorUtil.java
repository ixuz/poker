package com.antwika.eval.util;

import com.antwika.common.core.IHand;
import com.antwika.eval.data.Evaluation;
import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.exception.HandEvaluatorException;
import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;

import static com.antwika.common.util.BitmaskUtil.*;

public class HandEvaluatorUtil {
    public static IEvaluation evaluate(IHandProcessor handProcessor, long hand) {
        final IHandProcessorData data = HandProcessorDataUtil.createHandProcessorData(hand, handProcessor.maxKickerCount());

        for (int rankIndex = ACE_INDEX; rankIndex >= TWO_INDEX; rankIndex -= 1) {
            handProcessor.process(
                    data,
                    rankIndex,
                    Long.bitCount((TWOS << RANK_TO_INDEX[(int)RANKS[rankIndex]]) & hand),
                    0
            );
        }

        final IHandData handData = HandProcessorUtil.toHandData(data);
        final IEvaluation evaluation = handProcessor.toEvaluation(handData, 0);
        if (evaluation != null) {
            return evaluation;
        }

        return Evaluation.builder()
                .hand(handData.getHand())
                .handType(BitmaskUtil.NONE)
                .kickers(new int[]{})
                .kickersCount(0)
                .build();
    }

    public static IEvaluation evaluate(IHandProcessor handProcessor, IHand hand) {
        return evaluate(handProcessor, hand.getBitmask());
    }

    public static int compare(IHandProcessor handProcessor, long hand1, long hand2) throws HandEvaluatorException {
        if (Long.bitCount(hand1) != Long.bitCount(hand2)) throw new HandEvaluatorException();

        final IEvaluation handEvaluation1 = evaluate(handProcessor, hand1);
        final IEvaluation handEvaluation2 = evaluate(handProcessor, hand2);

        final long handTypeValue1 = handEvaluation1.getHandType();
        final long handTypeValue2 = handEvaluation2.getHandType();
        if (handTypeValue1 > handTypeValue2) return 1;
        if (handTypeValue1 < handTypeValue2) return -1;

        final int hand1KickersCount = handEvaluation1.getKickersCount();
        final int hand2KickersCount = handEvaluation2.getKickersCount();
        if (hand1KickersCount != hand2KickersCount) throw new HandEvaluatorException();

        for (int i = 0; i < hand1KickersCount; i++) {
            final int hand1Kicker = handEvaluation1.getKickers()[i];
            final int hand2Kicker = handEvaluation2.getKickers()[i];
            if (hand1Kicker > hand2Kicker) return 1;
            if (hand1Kicker < hand2Kicker) return -1;
        }

        return 0;
    }

    public static int compare(IHandProcessor handProcessor, IHand hand1, IHand hand2) throws HandEvaluatorException {
        return compare(handProcessor, hand1.getBitmask(), hand2.getBitmask());
    }

    public static int compare(IHandProcessor handProcessor, String hand1, String hand2) throws HandEvaluatorException {
        try {
            return compare(handProcessor, HandUtil.fromNotation(hand1), HandUtil.fromNotation(hand2));
        } catch (NotationException e) {
            throw new HandEvaluatorException();
        }
    }
}
