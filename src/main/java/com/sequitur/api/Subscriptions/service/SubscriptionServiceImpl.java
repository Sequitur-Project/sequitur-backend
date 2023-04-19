package com.sequitur.api.Subscriptions.service;

import com.sequitur.api.Subscriptions.domain.model.Subscription;
import com.sequitur.api.Subscriptions.domain.repository.SubscriptionRepository;
import com.sequitur.api.Subscriptions.domain.service.SubscriptionService;
import com.sequitur.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public ResponseEntity<?> deleteSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "Id", subscriptionId));
        subscriptionRepository.delete(subscription);
        return ResponseEntity.ok().build();
    }

    @Override
    public Subscription updateSubscription(Long subscriptionId, Subscription subscriptionRequest) {
       Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "Id", subscriptionId));
        subscription.setPrice(subscriptionRequest.getPrice());
        subscription.setType(subscriptionRequest.getType());
        subscription.setDescription(subscriptionRequest.getDescription());


        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "Id", subscriptionId));
    }

    @Override
    public Page<Subscription> getAllSubscriptions(Pageable pageable) {
        return subscriptionRepository.findAll(pageable);
    }
}
