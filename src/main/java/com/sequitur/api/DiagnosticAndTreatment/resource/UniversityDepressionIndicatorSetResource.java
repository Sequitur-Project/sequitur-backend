package com.sequitur.api.DiagnosticAndTreatment.resource;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sequitur.api.IdentityAccessManagement.domain.model.University;
import com.sequitur.api.IdentityAccessManagement.resource.UniversityResource;
import lombok.Data;

@Data
public class UniversityDepressionIndicatorSetResource {
    private Long id;

    private double depressionPercentage;

    private double noDepressionPercentage;

    private int studentsQuantity;


}
