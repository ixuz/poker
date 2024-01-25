package com.antwika.parser.lines;

public record PlayerBetLine(
        String playerName,
        int amount
) implements ILine {}
