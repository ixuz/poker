package com.antwika.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Candidate {
    private Seat seat;
    private int amount;
}
