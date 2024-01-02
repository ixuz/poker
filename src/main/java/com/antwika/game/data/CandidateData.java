package com.antwika.game.data;

import lombok.Data;

@Data
public class CandidateData {
    private String potName = "Pot";
    private SeatData seat;
    private int amount;

    public CandidateData(SeatData seat, int amount) {
        setSeat(seat);
        setAmount(amount);
    }
}
