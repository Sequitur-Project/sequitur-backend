package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.service.ConversationService;
import com.sequitur.api.DataCollection.resource.ConversationResource;
import com.sequitur.api.DataCollection.resource.SaveConversationResource;
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

@Tag(name = "conversations", description = "Conversations API")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class ConversationController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ConversationService conversationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Conversations returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/students/{studentId}/conversations")
    public Page<ConversationResource> getAllConversationsByStudentId(
            @PathVariable(name = "studentId") Long studentId,
            Pageable pageable) {
        Page<Conversation> conversationPage = conversationService.getAllConversationsByStudentId(studentId, pageable);
        List<ConversationResource> resources = conversationPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<ConversationResource>(resources, pageable, resources.size());
    }

    @GetMapping("/students/{studentId}/conversations/{conversationId}")
    public ConversationResource getConversationByIdAndStudentId(@PathVariable(name = "studentId") Long studentId,
                                             @PathVariable(name = "conversationId") Long conversationId) {
        return convertToResource(conversationService.getConversationByIdAndStudentId(studentId, conversationId));
    }

    @PostMapping("/students/{studentId}/conversations")
    public ConversationResource createConversation(@PathVariable(name = "studentId") Long studentId,
                                   @Valid @RequestBody SaveConversationResource resource) {
        return convertToResource(conversationService.createConversation(studentId, convertToEntity(resource)));

    }


    @DeleteMapping("/students/{studentId}/conversations/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable(name = "studentId") Long studentId,
                                        @PathVariable(name = "conversationId") Long conversationId) {
        return conversationService.deleteConversation(studentId, conversationId);
    }

    @Operation(summary = "Get Conversations", description = "Get All Conversations by Pages", tags = { "conversations" })
    @GetMapping("/conversations")
    public Page<ConversationResource> getAllConversations(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<Conversation> conversationsPage = conversationService.getAllConversations(pageable);
        List<ConversationResource> resources = conversationsPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<ConversationResource>(resources,pageable , resources.size());
    }

    private Conversation convertToEntity(SaveConversationResource resource) {
        return mapper.map(resource, Conversation.class);
    }

    private ConversationResource convertToResource(Conversation entity) {
        return mapper.map(entity, ConversationResource.class);
    }
}
