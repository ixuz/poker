package com.antwika.handhistory.line;

public record UncalledBetLine(
        int amount,
        String playerName
) implements ILine {}
