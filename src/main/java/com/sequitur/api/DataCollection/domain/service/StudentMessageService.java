package com.sequitur.api.DataCollection.domain.service;

import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface StudentMessageService {
    ResponseEntity<?> deleteStudentMessage(Long studentMessageId, Long conversationId);

    StudentMessage updateStudentMessage(Long studentMessageId, Long conversationId, StudentMessage studentMessageRequest);

    StudentMessage createStudentMessage(Long conversationId, StudentMessage studentMessage);

    StudentMessage getStudentMessageById(Long studentMessageId);

    Page<StudentMessage> getAllStudentMessages(Pageable pageable);

    Page<StudentMessage> getAllStudentMessagesByConversationId(Long conversationId, Pageable pageable);

    StudentMessage getStudentMessageByIdAndConversationId(Long conversationId, Long studentMessageId);


}
