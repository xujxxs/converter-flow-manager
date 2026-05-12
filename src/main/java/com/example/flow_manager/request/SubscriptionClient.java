package com.example.flow_manager.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.flow_manager.model.dto.SubscriptionTypeResponse;

@FeignClient(name = "SUBSCRIPTION")
public interface SubscriptionClient {

    @GetMapping("/api/v1/subscription/status")
    SubscriptionTypeResponse getSubscription(@RequestHeader("X-User-Login") String username);
}
