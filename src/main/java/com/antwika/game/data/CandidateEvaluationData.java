package com.antwika.game.data;

import com.antwika.eval.core.IEvaluation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateEvaluationData {
    private CandidateData candidate;
    private IEvaluation evaluation;
    private String groupId;
}
