package com.antwika.handhistory.line;

public record BlindLine(
    String playerName,
    String blindName,
    int amount
) implements ILine {}
