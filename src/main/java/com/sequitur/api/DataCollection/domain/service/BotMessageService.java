package com.sequitur.api.DataCollection.domain.service;

import com.sequitur.api.DataCollection.domain.model.BotMessage;
import com.sequitur.api.DataCollection.domain.model.StudentMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BotMessageService {
    BotMessage getBotMessageById(Long botMessageId);

    Page<BotMessage> getAllBotMessages(Pageable pageable);

    Page<BotMessage> getAllBotMessagesByConversationId(Long conversationId, Pageable pageable);

    BotMessage getBotMessageByIdAndConversationId(Long conversationId, Long botMessageId);
}
