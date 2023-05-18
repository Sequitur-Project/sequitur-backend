package com.sequitur.api.DataCollection.service;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.repository.ConversationRepository;
import com.sequitur.api.DataCollection.domain.service.ConversationService;
import com.sequitur.api.IdentityAccessManagement.domain.model.Student;
import com.sequitur.api.IdentityAccessManagement.domain.repository.StudentRepository;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public Conversation getConversationByStudentId(Long studentId) {
        return conversationRepository.findByStudentId(studentId);
    }

    @Override
    public Conversation getConversationByIdAndStudentId(Long studentId, Long conversationId) {
        return conversationRepository.findByIdAndStudentId(conversationId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation not found with Id " + conversationId +
                                " and StudentId " + studentId));
    }

    @Override
    public ResponseEntity<?> deleteConversation(Long studentId, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "Id", conversationId));
        conversationRepository.delete(conversation);
        return ResponseEntity.ok().build();
    }

    @Override
    public Conversation createConversation(Long studentId, Conversation conversation) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        conversation.setStudent(student);
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "Id", conversationId));
    }

    @Override
    public Page<Conversation> getAllConversations(Pageable pageable) {
        return conversationRepository.findAll(pageable);
    }
}
