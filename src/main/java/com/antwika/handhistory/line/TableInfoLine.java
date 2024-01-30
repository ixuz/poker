package com.antwika.handhistory.line;

public record TableInfoLine(
        String tableName,
        int seatCount,
        int buttonAt
) implements ILine {}
