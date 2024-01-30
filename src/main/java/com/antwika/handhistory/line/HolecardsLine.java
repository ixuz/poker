package com.antwika.handhistory.line;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.CardUtil;
import com.antwika.common.util.HandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record HolecardsLine(
        String playerName,
        long card1,
        long card2
) implements ILine {
    private static final Logger logger = LoggerFactory.getLogger(HolecardsLine.class);

    public Long holecards() {
        return card1() | card2();
    }
}
