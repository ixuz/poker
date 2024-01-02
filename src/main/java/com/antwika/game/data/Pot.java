package com.antwika.game.data;

import lombok.Data;

import java.util.List;

@Data
public class Pot {
    private String name = "Pot";
    private int amountPerCandidate;
    private List<CandidateData> candidates;
    private int totalAmount;

    public Pot(int amountPerCandidate, List<CandidateData> candidates) {
        setAmountPerCandidate(amountPerCandidate);
        setCandidates(candidates);
        setTotalAmount(candidates.size() * amountPerCandidate);
    }
}
