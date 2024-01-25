package com.antwika.parser.lines;

public record CollectedPotLine (
        String playerName,
        int amount,
        String potName
) implements ILine {}
