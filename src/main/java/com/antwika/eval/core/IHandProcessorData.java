package com.antwika.eval.core;

public interface IHandProcessorData {
    long getHand();
    void setHand(long hand);
    int[] getKickers();
    long getHandType();
    void setHandType(long handType);
    int getHighCardKickerCount();
    void setHighCardKickerCount(int highCardKickerCount);
}
