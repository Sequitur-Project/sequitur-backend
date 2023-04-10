package com.sequitur.api.DataCollection.service;

import com.google.cloud.dialogflow.v2.*;
import com.sequitur.api.DataCollection.domain.model.BotMessage;
import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import com.sequitur.api.DataCollection.domain.repository.BotMessageRepository;
import com.sequitur.api.DataCollection.domain.repository.ConversationRepository;
import com.sequitur.api.DataCollection.domain.repository.StudentMessageRepository;
import com.sequitur.api.DataCollection.domain.service.StudentMessageService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentMessageServiceImpl implements StudentMessageService {

    @Autowired
    private StudentMessageRepository studentMessageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private BotMessageRepository botMessageRepository;

    @Override
    public ResponseEntity<?> deleteStudentMessage(Long studentMessageId, Long conversationId) {
        StudentMessage studentMessage = studentMessageRepository.findById(studentMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentMessage", "Id", studentMessageId));
        studentMessageRepository.delete(studentMessage);
        return ResponseEntity.ok().build();
    }

    @Override
    public StudentMessage updateStudentMessage(Long studentMessageId, Long conversationId, StudentMessage studentMessageRequest) {
        StudentMessage studentMessage = studentMessageRepository.findById(studentMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentMessage", "Id", studentMessageId));
        studentMessage.setMessage(studentMessageRequest.getMessage());
        return studentMessageRepository.save(studentMessage);
    }

    @Override
    public StudentMessage createStudentMessage(Long conversationId, StudentMessage studentMessage) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new ResourceNotFoundException("Conversation", "Id", conversationId));
        studentMessage.setConversation(conversation);
        StudentMessage savedMessage = studentMessageRepository.save(studentMessage);

        // call Dialogflow to get bots response
        String projectId = "sequitur-yqvh";
        String sessionId = conversationId.toString();
        String languageCode = "es";
        String text = studentMessage.getMessage();
        try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            QueryResult queryResult = response.getQueryResult();
            String fulfillmentText = queryResult.getFulfillmentText();
            // create new BotMessage and add it to conversation
            BotMessage botMessage = new BotMessage();
            botMessage.setMessage(fulfillmentText);
            botMessage.setConversation(conversation);
            botMessageRepository.save(botMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedMessage;
    }

    @Override
    public StudentMessage getStudentMessageById(Long studentMessageId) {
        return studentMessageRepository.findById(studentMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentMessage", "Id", studentMessageId));
    }

    @Override
    public Page<StudentMessage> getAllStudentMessages(Pageable pageable) {
        return studentMessageRepository.findAll(pageable);
    }

    @Override
    public Page<StudentMessage> getAllStudentMessagesByConversationId(Long conversationId, Pageable pageable) {
        return studentMessageRepository.findByConversationId(conversationId, pageable);
    }

    @Override
    public StudentMessage getStudentMessageByIdAndConversationId(Long conversationId, Long studentMessageId) {
        return studentMessageRepository.findByIdAndConversationId(studentMessageId, conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "StudentMessage not found with Id " + studentMessageId +
                                " and ConversationId " + conversationId));
    }
}