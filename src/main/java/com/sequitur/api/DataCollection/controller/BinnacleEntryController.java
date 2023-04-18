package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.BinnacleEntry;
import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.service.BinnacleEntryService;
import com.sequitur.api.DataCollection.domain.service.ConversationService;
import com.sequitur.api.DataCollection.resource.BinnacleEntryResource;
import com.sequitur.api.DataCollection.resource.ConversationResource;
import com.sequitur.api.DataCollection.resource.SaveBinnacleEntryResource;
import com.sequitur.api.DataCollection.resource.SaveConversationResource;
import com.sequitur.api.IdentityAccessManagement.resource.PsychologistResource;
import com.sequitur.api.IdentityAccessManagement.resource.SavePsychologistResource;
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
import java.util.stream.Collectors;

@Tag(name = "binnacle_entries", description = "BinnacleEntries API")
@RestController
@RequestMapping("/api")
public class BinnacleEntryController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BinnacleEntryService binnacleEntryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Binnacle Entries returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/binnacles/{binnacleId}/binnacleEntries")
    public Page<BinnacleEntryResource> getAllBinnacleEntriesByBinnacleId(
            @PathVariable(name = "binnacleId") Long binnacleId,
            Pageable pageable) {
        Page<BinnacleEntry> binnacleEntryPage = binnacleEntryService.getAllBinnacleEntriesByBinnacleId(binnacleId, pageable);
        List<BinnacleEntryResource> resources = binnacleEntryPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<BinnacleEntryResource>(resources, pageable, resources.size());
    }

    @GetMapping("/binnacles/{binnacleId}/binnacleEntries/{binnacleEntryId}")
    public BinnacleEntryResource getBinnacleEntryByIdAndBinnacleId(@PathVariable(name = "binnacleEntryId") Long binnacleEntryId,
                                                                @PathVariable(name = "binnacleId") Long binnacleId) {
        return convertToResource(binnacleEntryService.getBinnacleEntryByIdAndBinnacleId(binnacleEntryId, binnacleId));
    }

    @PostMapping("/binnacles/{binnacleId}/binnacleEntries")
    public BinnacleEntryResource createBinnacleEntry(@PathVariable(name = "binnacleId") Long binnacleId,
                                                   @Valid @RequestBody SaveBinnacleEntryResource resource) {
        return convertToResource(binnacleEntryService.createBinnacleEntry(binnacleId, convertToEntity(resource)));

    }

    @PutMapping("/binnacles/{binnacleId}/binnacleEntries/{binnacleEntryId}")
    public BinnacleEntryResource updateBinnacleEntry(@PathVariable(name = "binnacleId") Long binnacleId,
                                                   @PathVariable(name = "binnacleEntryId") Long binnacleEntryId,
                                                   @Valid @RequestBody SaveBinnacleEntryResource resource) {
        return convertToResource(binnacleEntryService.updateBinnacleEntry(binnacleId, binnacleEntryId, convertToEntity(resource)));
    }


    @DeleteMapping("/binnacles/{binnacleId}/binnacleEntries/{binnacleEntryId}")
    public ResponseEntity<?> deleteBinnacleEntry(@PathVariable(name = "binnacleEntryId") Long binnacleEntryId,
                                                @PathVariable(name = "binnacleId") Long binnacleId) {
        return binnacleEntryService.deleteBinnacleEntry(binnacleEntryId, binnacleId);
    }

    @Operation(summary = "Get Binnacle Entries", description = "Get All Binnacle Entries by Pages", tags = { "binnacle_entries" })
    @GetMapping("/binnacleEntries")
    public Page<BinnacleEntryResource> getAllBinnacleEntries(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<BinnacleEntry> binnacleEntryPage = binnacleEntryService.getAllBinnacleEntries(pageable);
        List<BinnacleEntryResource> resources = binnacleEntryPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<BinnacleEntryResource>(resources,pageable , resources.size());
    }

    private BinnacleEntry convertToEntity(SaveBinnacleEntryResource resource) {
        return mapper.map(resource, BinnacleEntry.class);
    }

    private BinnacleEntryResource convertToResource(BinnacleEntry entity) {
        return mapper.map(entity, BinnacleEntryResource.class);
    }
}
