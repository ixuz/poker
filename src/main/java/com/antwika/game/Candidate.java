package com.antwika.game;

import lombok.Data;

@Data
public class Candidate {
    private String potName = "Pot";
    private Seat seat;
    private int amount;

    public Candidate(Seat seat, int amount) {
        setSeat(seat);
        setAmount(amount);
    }
}
