package com.sequitur.api.DataCollection.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;


@Data
public class BinnacleEntryResource extends AuditModel {

    private Long id;

    private String emoji;

    private String feeling;

    private String reason;

    private String extraText;
}
