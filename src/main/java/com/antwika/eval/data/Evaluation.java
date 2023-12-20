package com.antwika.eval.data;

import com.antwika.eval.core.IEvaluation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Evaluation implements IEvaluation {
    private long hand;
    private long handType;
    private int[] kickers;
    private int kickersCount;
}
