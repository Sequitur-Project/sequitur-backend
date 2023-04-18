package com.sequitur.api.DataCollection.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;



@Data
public class SaveBinnacleEntryResource extends AuditModel {

    private String emoji;

    private String feeling;

    private String reason;

    private String extraText;
}
