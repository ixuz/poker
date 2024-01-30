package com.antwika.handhistory.line;

public record PlayerRaiseLine(
        String playerName,
        int amount
) implements ILine {}
