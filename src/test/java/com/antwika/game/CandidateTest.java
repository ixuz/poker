package com.antwika.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CandidateTest {
    @Test
    public void equality() {
        final Seat seat = new Seat();
        final Candidate candidate1 = new Candidate(seat, 100);
        final Candidate candidate2 = new Candidate(seat, 100);

        assertEquals(candidate1, candidate2);
        assertTrue(candidate1.equals(candidate2));

        final List<Candidate> candidates1 = List.of(candidate1, candidate2);
        final List<Candidate> candidates2 = List.of(candidate1, candidate2);
        assertEquals(candidates1, candidates2);

        final List<Candidate> copyCandidates = new ArrayList<>(candidates1);
        copyCandidates.removeAll(candidates2);
        assertEquals(0, copyCandidates.size());
    }
}
