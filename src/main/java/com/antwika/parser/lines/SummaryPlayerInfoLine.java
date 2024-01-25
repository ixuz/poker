package com.antwika.parser.lines;

public record SummaryPlayerInfoLine(
        int seatId,
        String playerName,
        int stack
) implements ILine {}
