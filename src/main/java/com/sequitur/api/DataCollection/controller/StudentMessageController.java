package com.sequitur.api.DataCollection.controller;

import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.DataCollection.domain.service.StudentMessageService;
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

@Tag(name = "studentMessages", description = "StudentMessages API")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class StudentMessageController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private StudentMessageService studentMessageService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All StudentMessages returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/conversations/{conversationId}/studentMessages")
    public Page<StudentMessageResource> getAllStudentMessagesByConversationId(
            @PathVariable(name = "conversationId") Long conversationId,
            Pageable pageable) {
        Page<StudentMessage> studentMessagePage = studentMessageService.getAllStudentMessagesByConversationId(conversationId, pageable);
        List<StudentMessageResource> resources = studentMessagePage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());
        return new PageImpl<StudentMessageResource>(resources, pageable, resources.size());
    }

    @GetMapping("/conversations/{conversationId}/studentMessages/{studentMessageId}")
    public StudentMessageResource getStudentMessageByIdAndConversationId(@PathVariable(name = "conversationId") Long conversationId,
                                             @PathVariable(name = "studentMessageId") Long studentMessageId) {
        return convertToResource(studentMessageService.getStudentMessageByIdAndConversationId(conversationId, studentMessageId));
    }


    @PostMapping("/conversations/{conversationId}/studentMessages")
    public StudentMessageResource createStudentMessage(@PathVariable(name = "conversationId") Long conversationId,
                                   @Valid @RequestBody SaveStudentMessageResource resource) {
        return convertToResource(studentMessageService.createStudentMessage(conversationId, convertToEntity(resource)));

    }

    @PutMapping("/conversations/{conversationId}/studentMessages/{studentMessageId}")
    public StudentMessageResource updateStudentMessage(@PathVariable(name = "conversationId") Long conversationId,
                                   @PathVariable(name = "studentMessageId") Long studentMessageId,
                                   @Valid @RequestBody SaveStudentMessageResource resource) {
        return convertToResource(studentMessageService.updateStudentMessage(studentMessageId, conversationId, convertToEntity(resource)));
    }

    @DeleteMapping("/conversations/{conversationId}/studentMessages/{studentMessageId}")
    public ResponseEntity<?> deleteStudentMessage(@PathVariable(name = "conversationId") Long conversationId,
                                        @PathVariable(name = "studentMessageId") Long studentMessageId) {
        return studentMessageService.deleteStudentMessage(studentMessageId, conversationId);
    }

    @Operation(summary = "Get StudentMessages", description = "Get All StudentMessages by Pages", tags = { "studentMessages" })
    @GetMapping("/studentMessages")
    public Page<StudentMessageResource> getAllStudentMessages(
            @Parameter(description="Pageable Parameter")
            Pageable pageable) {
        Page<StudentMessage> studentMessagesPage = studentMessageService.getAllStudentMessages(pageable);
        List<StudentMessageResource> resources = studentMessagesPage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<StudentMessageResource>(resources,pageable , resources.size());
    }


    private StudentMessage convertToEntity(SaveStudentMessageResource resource) {
        return mapper.map(resource, StudentMessage.class);
    }

    private StudentMessageResource convertToResource(StudentMessage entity) {
        return mapper.map(entity, StudentMessageResource.class);
    }
}
