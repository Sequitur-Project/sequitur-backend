package com.sequitur.api.DiagnosticAndTreatment.domain.repository;

import com.sequitur.api.DataCollection.domain.model.Conversation;
import com.sequitur.api.DiagnosticAndTreatment.domain.model.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result,Long> {

    Optional<Result> findByIdAndConversationId(Long id, Long conversationId);
}
