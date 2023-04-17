package com.sequitur.api.DataCollection.resource;

import lombok.Data;

import java.util.UUID;

@Data
public class TrainingPhraseResource {
    private String id;
    private UUID intentId;
    private String text;
}
