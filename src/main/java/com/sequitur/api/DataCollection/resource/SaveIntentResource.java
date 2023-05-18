package com.sequitur.api.DataCollection.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;

@Data
public class SaveIntentResource extends AuditModel {
    private String displayName;
}
