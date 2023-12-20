package com.antwika.eval.core;

import com.antwika.common.core.IHand;
import com.antwika.eval.exception.HandEvaluatorException;

public interface IHandEvaluator {
    IEvaluation evaluate(IHand hand) throws HandEvaluatorException;
    IEvaluation evaluate(long hand);
}
