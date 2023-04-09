package com.sequitur.api.Subscriptions.domain.service;

import com.sequitur.api.Subscriptions.domain.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface SubscriptionService {

    ResponseEntity<?> deleteSubscription(Long subscriptionId);

    Subscription updateSubscription(Long subscriptionId, Subscription subscriptionRequest);

    Subscription createSubscription(Subscription subscription);

    Subscription getSubscriptionById(Long subscriptionId);

    Page<Subscription> getAllSubscriptions(Pageable pageable);

}
