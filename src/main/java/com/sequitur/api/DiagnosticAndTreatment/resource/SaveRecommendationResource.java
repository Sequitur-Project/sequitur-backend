package com.sequitur.api.DiagnosticAndTreatment.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;

@Data
public class SaveRecommendationResource extends AuditModel {
    private String text;
}
