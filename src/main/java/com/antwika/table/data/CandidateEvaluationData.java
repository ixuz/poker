package com.antwika.table.data;

import com.antwika.eval.core.IEvaluation;

public record CandidateEvaluationData(
    CandidateData candidate,
    IEvaluation evaluation,
    String groupId
) {}
