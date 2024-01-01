package com.antwika.eval.core;

import com.antwika.common.core.IHand;

public interface IHandEvaluator {
    IEvaluation evaluate(IHand hand);
    IEvaluation evaluate(long hand);
}
