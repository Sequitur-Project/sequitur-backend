package com.sequitur.api.DataCollection.resource;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;

import java.util.List;


@Data
public class ConversationResource extends AuditModel {
    private Long id;

    private List<StudentMessageResource> studentMessages;
}
