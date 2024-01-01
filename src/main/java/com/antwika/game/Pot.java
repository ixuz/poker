package com.antwika.game;

import lombok.Data;

import java.util.List;

@Data
public class Pot {
    private String name = "Pot";
    private int amountPerCandidate;
    private List<Candidate> candidates;
    private int totalAmount;

    public Pot(int amountPerCandidate, List<Candidate> candidates) {
        setAmountPerCandidate(amountPerCandidate);
        setCandidates(candidates);
        setTotalAmount(candidates.size() * amountPerCandidate);
    }
}
