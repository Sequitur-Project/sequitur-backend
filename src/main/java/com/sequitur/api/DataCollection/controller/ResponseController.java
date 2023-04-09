package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.Response;
import com.sequitur.api.DataCollection.domain.service.ResponseService;
import com.sequitur.api.DataCollection.resource.ResponseResource;
import com.sequitur.api.DataCollection.resource.SaveResponseResource;
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

@Tag(name = "responses", description = "Responses API")
@RestController
@RequestMapping("/api")
public class ResponseController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ResponseService responseService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Responses returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/intents/{intentId}/responses")
    public Page<ResponseResource> getAllResponsesByIntentId(
            @PathVariable(name = "intentId") UUID intentId,
            Pageable pageable) {
        Page<Response> responsePage = responseService.getAllResponsesByIntentId(intentId, pageable);
        List<ResponseResource> resources = responsePage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<ResponseResource>(resources, pageable, resources.size());
    }

    @GetMapping("/intents/{intentId}/responses/{responseId}")
    public ResponseResource getResponseByIdAndIntentId(@PathVariable(name = "intentId") UUID intentId,
                                             @PathVariable(name = "responseId") String responseId) {
        return convertToResource(responseService.getResponseByIdAndIntentId(intentId, responseId));
    }

    @PostMapping("/intents/{intentId}/responses")
    public ResponseResource createResponse(@PathVariable(name = "intentId") UUID intentId,
                                   @Valid @RequestBody SaveResponseResource resource) {
        return convertToResource(responseService.createResponse(intentId, convertToEntity(resource)));

    }

    @PutMapping("/intents/{intentId}/responses/{responseId}")
    public ResponseResource updateResponse(@PathVariable(name = "intentId") UUID intentId,
                                   @PathVariable(name = "responseId") String responseId,
                                   @Valid @RequestBody SaveResponseResource resource) {
        return convertToResource(responseService.updateResponse(responseId, intentId, convertToEntity(resource)));
    }

    @DeleteMapping("/intents/{intentId}/responses/{responseId}")
    public ResponseEntity<?> deleteResponse(@PathVariable(name = "intentId") UUID intentId,
                                        @PathVariable(name = "responseId") String responseId) {
        return responseService.deleteResponse(responseId,intentId);
    }

    @Operation(summary = "Get Responses", description = "Get All Responses by Pages", tags = { "responses" })
    @GetMapping("/responses")
    public Page<ResponseResource> getAllResponses(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Response> responsesPage = responseService.getAllResponses(pageable);
        List<ResponseResource> resources = responsesPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<ResponseResource>(resources,pageable , resources.size());
    }

    private Response convertToEntity(SaveResponseResource resource) {
        return mapper.map(resource, Response.class);
    }

    private ResponseResource convertToResource(Response entity) {
        return mapper.map(entity, ResponseResource.class);
    }
}
