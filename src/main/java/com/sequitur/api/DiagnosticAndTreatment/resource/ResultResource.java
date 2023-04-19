package com.sequitur.api.DiagnosticAndTreatment.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;

@Data
public class ResultResource extends AuditModel {
    private Long id;
    private int score;
}
