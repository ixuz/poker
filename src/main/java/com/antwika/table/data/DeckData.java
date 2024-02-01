package com.antwika.table.data;

import com.antwika.table.common.Prng;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeckData {
    private Prng prng;
    private List<Long> cards = new ArrayList<>();

    public DeckData(long prngSeed) {
        this.prng = new Prng(prngSeed);
    }
}
