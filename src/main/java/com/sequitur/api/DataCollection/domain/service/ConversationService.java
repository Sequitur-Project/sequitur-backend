package com.sequitur.api.DataCollection.domain.service;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ConversationService {

    Conversation getConversationByStudentId(Long studentId);

    Conversation getConversationByIdAndStudentId(Long studentId, Long conversationId);

    ResponseEntity<?> deleteConversation(Long studentId, Long conversationId);

    Conversation createConversation(Long studentId, Conversation conversation);

    Conversation getConversationById(Long conversationId);

    Page<Conversation> getAllConversations(Pageable pageable);

}
