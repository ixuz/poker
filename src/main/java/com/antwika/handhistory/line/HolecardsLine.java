package com.antwika.handhistory.line;

public record HolecardsLine(
        String playerName,
        long card1,
        long card2
) implements ILine {
    public Long holecards() {
        return card1() | card2();
    }
}
