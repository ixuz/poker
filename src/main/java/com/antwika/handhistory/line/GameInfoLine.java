package com.antwika.handhistory.line;

public record GameInfoLine(
    long handId,
    String gameType,
    int smallBlind,
    int bigBlind,
    String timestamp
) implements ILine {}
