package com.sequitur.api.DiagnosticAndTreatment.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;

@Data
public class RecommendationResource extends AuditModel {
    private Long id;
    private String text;
}
