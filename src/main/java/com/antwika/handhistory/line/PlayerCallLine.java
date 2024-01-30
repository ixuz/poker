package com.antwika.handhistory.line;

public record PlayerCallLine(
        String playerName,
        int amount
) implements ILine {}
