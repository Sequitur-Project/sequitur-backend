package com.sequitur.api.DataCollection.domain.repository;

import com.sequitur.api.DataCollection.domain.model.BotMessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
    Page<BotMessage> findByConversationId(Long conversationId, Pageable pageable);
    Optional<BotMessage> findByIdAndConversationId(Long id, Long conversationId);
}
