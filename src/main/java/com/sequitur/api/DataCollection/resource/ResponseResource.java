package com.sequitur.api.DataCollection.resource;

import lombok.Data;

import java.util.UUID;

@Data
public class ResponseResource {
    private String id;
    private UUID intentId;
    private String messageText;
}
