package com.antwika.eval.data;

import com.antwika.eval.data.Evaluation;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluationTest {
    @Test
    @Tag("UnitTest")
    public void construct() {
        Evaluation.builder().build();
    }

    @Test
    @Tag("UnitTest")
    public void serializeToString() {
        assertEquals("Evaluation(hand=0, handType=0, kickers=null, kickersCount=0)", Evaluation.builder().build().toString());
    }

    @Test
    @Tag("UnitTest")
    public void getHand() {
        assertEquals(1L, Evaluation.builder().hand(1L).build().getHand());
    }

    @Test
    @Tag("UnitTest")
    public void getHandType() {
        assertEquals(1L, Evaluation.builder().handType(1L).build().getHandType());
    }

    @Test
    @Tag("UnitTest")
    public void getKickers() {
        final int[] kickers = new int[]{};
        assertEquals(kickers, Evaluation.builder().kickers(kickers).build().getKickers());
    }

    @Test
    @Tag("UnitTest")
    public void getKickersCount() {
        assertEquals(1, Evaluation.builder().kickersCount(1).build().getKickersCount());
    }
}
