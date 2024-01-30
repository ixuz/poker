package com.antwika.handhistory.line;

public record PlayerBetLine(
        String playerName,
        int amount,
        boolean allIn
) implements ILine {}
