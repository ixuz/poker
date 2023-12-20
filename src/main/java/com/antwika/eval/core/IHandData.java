package com.antwika.eval.core;

import java.io.Serializable;

public interface IHandData extends Serializable {
    long getHand();
    void setHand(long hand);
    int[] getKickers();
    void setKickers(int[] kickers);
    long getHandType();
    void setHandType(long handType);
    int getHighCardKickerCount();
    void setHighCardKickerCount(int highCardKickerCount);
}
