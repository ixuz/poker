package com.antwika.parser.lines;

public record PlayerRaiseLine(
        String playerName,
        int amount
) implements ILine {}
