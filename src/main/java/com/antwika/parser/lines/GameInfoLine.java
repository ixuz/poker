package com.antwika.parser.lines;

public record GameInfoLine(
    long handId,
    String gameType,
    int smallBlind,
    int bigBlind,
    String timestamp) implements ILine {}
