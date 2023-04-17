package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.Intent;
import com.sequitur.api.DataCollection.domain.service.IntentService;
import com.sequitur.api.DataCollection.resource.IntentResource;
import com.sequitur.api.DataCollection.resource.SaveIntentResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "intents", description = "Intents API")
@RestController
@RequestMapping("/api")
public class IntentController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IntentService intentService;

    @Operation(summary = "Get Intents", description = "Get All Intents by Pages", tags = { "intents" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Intents returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/intents")
    public Page<IntentResource> getAllIntents(Pageable pageable) {
        Page<Intent> intentsPage = intentService.getAllIntents(pageable);
        List<IntentResource> resources = intentsPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<>(resources, pageable, resources.size());
    }

    @Operation(summary = "Get Intent by Id", description = "Get a Intent by specifying Id", tags = { "intents" })
    @GetMapping("/intents/{id}")
    public IntentResource getIntentById(
            @Parameter(description="Intent Id")
            @PathVariable(name = "id") UUID intentId) {
        return convertToResource(intentService.getIntentById(intentId));
    }

    @PostMapping("/intents")
    public IntentResource createIntent(@Valid @RequestBody SaveIntentResource resource)  {
        Intent intent = convertToEntity(resource);
        return convertToResource(intentService.createIntent(intent));
    }

    @PutMapping("/intents/{id}")
    public IntentResource updateIntent(@PathVariable(name = "id") UUID intentId, @Valid @RequestBody SaveIntentResource resource) {
        Intent intent = convertToEntity(resource);
        return convertToResource(intentService.updateIntent(intentId, intent));
    }

    @DeleteMapping("/intents/{id}")
    public ResponseEntity<?> deleteIntent(@PathVariable(name = "id") UUID intentId) {
        return intentService.deleteIntent(intentId);
    }
    // Auto Mapper
    private Intent convertToEntity(SaveIntentResource resource) {
        return mapper.map(resource, Intent.class);
    }

    private IntentResource convertToResource(Intent entity) {
        return mapper.map(entity, IntentResource.class);
    }
}
