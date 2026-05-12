package com.example.flow_manager.service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.flow_manager.service.SubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionConsumer {

    private final SubscriptionService subscriptionService;

    @KafkaListener(topics = "${queue.kafka.topic.subscription-del-cache}", groupId = "${queue.kafka.group-id}")
    public void delCache(String username) {
        log.info("Received delete from cache subscription for user: {}", username);
        subscriptionService.delFromCache(username);
    }
}
