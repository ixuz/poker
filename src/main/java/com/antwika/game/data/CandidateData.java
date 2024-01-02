package com.antwika.game.data;

import com.antwika.game.data.Seat;
import lombok.Data;

@Data
public class CandidateData {
    private String potName = "Pot";
    private Seat seat;
    private int amount;

    public CandidateData(Seat seat, int amount) {
        setSeat(seat);
        setAmount(amount);
    }
}
