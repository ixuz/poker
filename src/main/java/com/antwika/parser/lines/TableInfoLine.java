package com.antwika.parser.lines;

public record TableInfoLine(
        String tableName,
        int seatCount,
        int buttonAt
) implements ILine {}
