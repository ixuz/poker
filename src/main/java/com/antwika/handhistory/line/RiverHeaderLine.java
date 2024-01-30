package com.antwika.handhistory.line;

public record RiverHeaderLine(
        long card1,
        long card2,
        long card3,
        long card4,
        long card5
) implements ILine {
    public long cards() {
        return card1 | card2 | card3 | card4 | card5;
    }
}
