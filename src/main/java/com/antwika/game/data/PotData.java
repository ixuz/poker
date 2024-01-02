package com.antwika.game.data;

import lombok.Data;

import java.util.List;

@Data
public class PotData {
    private String name = "Pot";
    private int amountPerCandidate;
    private List<CandidateData> candidates;
    private int totalAmount;

    public PotData(int amountPerCandidate, List<CandidateData> candidates) {
        setAmountPerCandidate(amountPerCandidate);
        setCandidates(candidates);
        setTotalAmount(candidates.size() * amountPerCandidate);
    }
}
