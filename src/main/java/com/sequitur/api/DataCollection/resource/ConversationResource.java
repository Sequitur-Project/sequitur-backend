package com.sequitur.api.DataCollection.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;


@Data
public class ConversationResource extends AuditModel {
    private Long id;
}
