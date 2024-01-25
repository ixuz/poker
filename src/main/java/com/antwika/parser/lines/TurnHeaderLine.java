package com.antwika.parser.lines;

public record TurnHeaderLine(
        long card1,
        long card2,
        long card3,
        long card4
) implements ILine {
    public long cards() {
        return card1 | card2 | card3 | card4;
    }
}
