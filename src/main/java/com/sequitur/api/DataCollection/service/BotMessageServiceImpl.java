package com.sequitur.api.DataCollection.service;

import com.sequitur.api.DataCollection.domain.model.BotMessage;
import com.sequitur.api.DataCollection.domain.repository.BotMessageRepository;
import com.sequitur.api.DataCollection.domain.repository.ConversationRepository;
import com.sequitur.api.DataCollection.domain.service.BotMessageService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BotMessageServiceImpl implements BotMessageService {

    @Autowired
    private BotMessageRepository botMessageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public BotMessage getBotMessageById(Long botMessageId) {
        return botMessageRepository.findById(botMessageId)
                .orElseThrow(() -> new ResourceNotFoundException("BotMessage", "Id", botMessageId));
    }

    @Override
    public Page<BotMessage> getAllBotMessages(Pageable pageable) {
        return botMessageRepository.findAll(pageable);
    }

    @Override
    public Page<BotMessage> getAllBotMessagesByConversationId(Long conversationId, Pageable pageable) {
        return botMessageRepository.findByConversationId(conversationId, pageable);
    }

    @Override
    public BotMessage getBotMessageByIdAndConversationId(Long conversationId, Long botMessageId) {
        return botMessageRepository.findByIdAndConversationId(botMessageId, conversationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BotMessage not found with Id " + botMessageId +
                                " and ConversationId " + conversationId));

    }
}
