package com.sequitur.api.DataCollection.resource;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.DataCollection.domain.model.TrainingPhrase;
import com.sequitur.api.SharedContext.domain.model.AuditModel;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class IntentResource extends AuditModel {
    private UUID id;
    private String displayName;
    @JsonManagedReference
    private List<TrainingPhrase> trainingPhrases;
    @JsonManagedReference
    private List<Response> responses;
}
