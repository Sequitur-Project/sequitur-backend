package com.sequitur.api.DataCollection.domain.service;

import com.sequitur.api.DataCollection.domain.model.Intent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IntentService {

    ResponseEntity<?> deleteIntent(UUID intentId);

    Intent updateIntent(UUID intentId, Intent intentRequest);

    Intent createIntent(Intent intent);

    Intent getIntentById(UUID intentId);

    Page<Intent> getAllIntents(Pageable pageable);

}
