package com.sequitur.api.DataCollection.resource;

import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;


@Data
public class BotMessageResource extends AuditModel {

    private Long id;
    private String message;
}
