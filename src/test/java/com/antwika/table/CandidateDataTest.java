package com.antwika.table;

import com.antwika.table.data.CandidateData;
import com.antwika.table.data.SeatData;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CandidateDataTest {
    @Test
    public void equality() {
        final SeatData seat = new SeatData();
        final CandidateData candidate1 = new CandidateData("Pot", seat, 100);
        final CandidateData candidate2 = new CandidateData("Pot", seat, 100);

        assertEquals(candidate1, candidate2);

        final List<CandidateData> candidates1 = List.of(candidate1, candidate2);
        final List<CandidateData> candidates2 = List.of(candidate1, candidate2);
        assertEquals(candidates1, candidates2);

        final List<CandidateData> copyCandidates = new ArrayList<>(candidates1);
        copyCandidates.removeAll(candidates2);
        assertEquals(0, copyCandidates.size());
    }
}
