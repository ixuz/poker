package com.antwika.parser.lines;

public record PlayerCallLine(
        String playerName,
        int amount
) implements ILine {}
