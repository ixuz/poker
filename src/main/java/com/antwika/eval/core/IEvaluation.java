package com.antwika.eval.core;

import java.io.Serializable;

public interface IEvaluation extends Serializable {
    long getHand();
    long getHandType();
    int[] getKickers();
    int getKickersCount();
}
