package com.sequitur.api.DataCollection.resource;

import lombok.Data;

import java.util.UUID;

@Data
public class IntentResource {
    private UUID id;
    private String displayName;
}
