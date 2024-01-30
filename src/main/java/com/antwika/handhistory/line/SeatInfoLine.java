package com.antwika.handhistory.line;

public record SeatInfoLine(
        int seatId,
        String playerName,
        int stack
) implements ILine {}
