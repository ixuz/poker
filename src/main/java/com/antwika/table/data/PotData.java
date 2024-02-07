package com.antwika.table.data;

import java.util.List;

public class PotData {
    private String name;
    private int amountPerCandidate;
    private final List<CandidateData> candidates;

    public PotData(String name, int amountPerCandidate, List<CandidateData> candidates) {
        this.name = name;
        this.amountPerCandidate = amountPerCandidate;
        this.candidates = candidates;
    }

    public void name(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public int amountPerCandidate() {
        return amountPerCandidate;
    }

    public void amountPerCandidate(int amountPerCandidate) {
        this.amountPerCandidate = amountPerCandidate;
    }

    public List<CandidateData> candidates() {
        return candidates;
    }

    public int totalAmount() {
        return amountPerCandidate * candidates.size();
    }
}
