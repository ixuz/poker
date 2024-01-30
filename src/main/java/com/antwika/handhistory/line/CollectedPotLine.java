package com.antwika.handhistory.line;

public record CollectedPotLine (
        String playerName,
        int amount,
        String potName
) implements ILine {}
