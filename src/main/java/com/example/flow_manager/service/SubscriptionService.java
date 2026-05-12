package com.example.flow_manager.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.flow_manager.model.dto.SubscriptionTypeResponse;
import com.example.flow_manager.request.SubscriptionClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionClient subscriptionClient;

    @Cacheable(value = "type_subscription", key = "#username")
    public SubscriptionTypeResponse getUserSubscription(String username) {
        return subscriptionClient.getSubscription(username);
    }

    @CacheEvict(value = "type_subscription", key = "#username")
    public void delFromCache(String username) { }
}
