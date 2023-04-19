package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.BotMessage;
import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.DataCollection.domain.service.BotMessageService;
import com.sequitur.api.DataCollection.domain.service.StudentMessageService;
import com.sequitur.api.DataCollection.resource.BotMessageResource;
import com.sequitur.api.DataCollection.resource.SaveStudentMessageResource;
import com.sequitur.api.DataCollection.resource.StudentMessageResource;
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

@Tag(name = "botMessages", description = "BotMessages API")
@RestController
@RequestMapping("/api")
public class BotMessageController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BotMessageService botMessageService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All BotMessages returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/conversations/{conversationId}/botMessages")
    public Page<BotMessageResource> getAllBotMessagesByConversationId(
            @PathVariable(name = "conversationId") Long conversationId,
            Pageable pageable) {
        Page<BotMessage> botMessagePage = botMessageService.getAllBotMessagesByConversationId(conversationId, pageable);
        List<BotMessageResource> resources = botMessagePage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<BotMessageResource>(resources, pageable, resources.size());
    }

    @GetMapping("/conversations/{conversationId}/botMessages/{botMessageId}")
    public BotMessageResource getBotMessageByIdAndConversationId(@PathVariable(name = "conversationId") Long conversationId,
                                                                         @PathVariable(name = "botMessageId") Long botMessageId) {
        return convertToResource(botMessageService.getBotMessageByIdAndConversationId(conversationId, botMessageId));
    }

    @Operation(summary = "Get BotMessages", description = "Get All BotMessages by Pages", tags = { "botMessages" })
    @GetMapping("/botMessages")
    public Page<BotMessageResource> getAllBotMessages(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<BotMessage> botMessagesPage = botMessageService.getAllBotMessages(pageable);
        List<BotMessageResource> resources = botMessagesPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<BotMessageResource>(resources,pageable , resources.size());
    }


    private StudentMessage convertToEntity(SaveStudentMessageResource resource) {
        return mapper.map(resource, StudentMessage.class);
    }

    private BotMessageResource convertToResource(BotMessage entity) {
        return mapper.map(entity, BotMessageResource.class);
    }
}
