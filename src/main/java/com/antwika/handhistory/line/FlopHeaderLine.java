package com.antwika.handhistory.line;

public record FlopHeaderLine (
        long card1,
        long card2,
        long card3
) implements ILine {
    public long cards() {
        return card1 | card2 | card3;
    }
}
