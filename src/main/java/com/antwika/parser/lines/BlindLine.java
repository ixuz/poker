package com.antwika.parser.lines;

public record BlindLine(
    String playerName,
    String blindName,
    int amount
) implements ILine {}
