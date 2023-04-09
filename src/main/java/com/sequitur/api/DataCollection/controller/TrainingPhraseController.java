package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.TrainingPhrase;
import com.sequitur.api.DataCollection.domain.service.TrainingPhraseService;
import com.sequitur.api.DataCollection.resource.SaveTrainingPhraseResource;
import com.sequitur.api.DataCollection.resource.TrainingPhraseResource;
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

@Tag(name = "trainingPhrases", description = "TrainingPhrases API")
@RestController
@RequestMapping("/api")
public class TrainingPhraseController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TrainingPhraseService trainingPhraseService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All TrainingPhrases returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/intents/{intentId}/trainingPhrases")
    public Page<TrainingPhraseResource> getAllTrainingPhrasesByIntentId(
            @PathVariable(name = "intentId") UUID intentId,
            Pageable pageable) {
        Page<TrainingPhrase> trainingPhrasePage = trainingPhraseService.getAllTrainingPhrasesByIntentId(intentId, pageable);
        List<TrainingPhraseResource> resources = trainingPhrasePage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<TrainingPhraseResource>(resources, pageable, resources.size());
    }

    @GetMapping("/intents/{intentId}/trainingPhrases/{trainingPhraseId}")
    public TrainingPhraseResource getTrainingPhraseByIdAndIntentId(@PathVariable(name = "intentId") UUID intentId,
                                                       @PathVariable(name = "trainingPhraseId") String trainingPhraseId) {
        return convertToResource(trainingPhraseService.getTrainingPhraseByIdAndIntentId(intentId, trainingPhraseId));
    }

    @PostMapping("/intents/{intentId}/trainingPhrases")
    public TrainingPhraseResource createTrainingPhrase(@PathVariable(name = "intentId") UUID intentId,
                                           @Valid @RequestBody SaveTrainingPhraseResource resource) {
        return convertToResource(trainingPhraseService.createTrainingPhrase(intentId, convertToEntity(resource)));

    }

    @PutMapping("/intents/{intentId}/trainingPhrases/{trainingPhraseId}")
    public TrainingPhraseResource updateTrainingPhrase(@PathVariable(name = "intentId") UUID intentId,
                                           @PathVariable(name = "trainingPhraseId") String trainingPhraseId,
                                           @Valid @RequestBody SaveTrainingPhraseResource resource) {
        return convertToResource(trainingPhraseService.updateTrainingPhrase(trainingPhraseId, intentId, convertToEntity(resource)));
    }

    @DeleteMapping("/intents/{intentId}/trainingPhrases/{trainingPhraseId}")
    public ResponseEntity<?> deleteTrainingPhrase(@PathVariable(name = "intentId") UUID intentId,
                                            @PathVariable(name = "trainingPhraseId") String trainingPhraseId) {
        return trainingPhraseService.deleteTrainingPhrase(trainingPhraseId,intentId);
    }

    @Operation(summary = "Get TrainingPhrases", description = "Get All TrainingPhrase by Pages", tags = { "trainingPhrases" })
    @GetMapping("/trainingPhrases")
    public Page<TrainingPhraseResource> getAllTrainingPhrase(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<TrainingPhrase> trainingPhrasesPage =trainingPhraseService.getAllTrainingPhrases(pageable);
        List<TrainingPhraseResource> resources = trainingPhrasesPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<TrainingPhraseResource>(resources,pageable , resources.size());
    }

    private TrainingPhrase convertToEntity(SaveTrainingPhraseResource resource) {
        return mapper.map(resource, TrainingPhrase.class);
    }

    private TrainingPhraseResource convertToResource(TrainingPhrase entity) {
        return mapper.map(entity, TrainingPhraseResource.class);
    }
}
