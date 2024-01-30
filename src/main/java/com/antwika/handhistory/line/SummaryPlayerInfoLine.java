package com.antwika.handhistory.line;

public record SummaryPlayerInfoLine(
        int seatId,
        String playerName,
        int stack
) implements ILine {}
