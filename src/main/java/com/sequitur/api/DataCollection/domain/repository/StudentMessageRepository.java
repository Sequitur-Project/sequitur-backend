package com.sequitur.api.DataCollection.domain.repository;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentMessageRepository extends JpaRepository<StudentMessage, Long> {
    Page<StudentMessage> findByConversationId(Long conversationId, Pageable pageable);
    Optional<StudentMessage> findByIdAndConversationId(Long id, Long conversationId);

}
