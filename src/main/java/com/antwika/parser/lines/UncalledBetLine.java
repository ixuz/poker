package com.antwika.parser.lines;

public record UncalledBetLine(
        int amount,
        String playerName
) implements ILine {}
