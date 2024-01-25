package com.antwika.parser.lines;

public record SeatInfoLine(
        int seatId,
        String playerName,
        int stack
) implements ILine {}
