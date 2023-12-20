package com.antwika.eval.core;

public interface IHandProcessor {
    int maxKickerCount();
    void process(IHandProcessorData data, int rankIndex, long rankCount, int kickerOffset);
    IEvaluation toEvaluation(IHandData handData, int kickerOffset);
}
