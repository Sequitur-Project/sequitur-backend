package com.sequitur.api.Subscriptions.domain.repository;

import com.sequitur.api.Subscriptions.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
}
