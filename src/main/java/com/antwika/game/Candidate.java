package com.antwika.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Candidate {
    private Seat seat;
    private int amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return amount == candidate.amount && Objects.equals(seat, candidate.seat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seat, amount);
    }
}
