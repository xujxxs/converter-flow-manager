package com.example.flow_manager.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "SUBSCRIPTION")
public interface SubscriptionRequest {

    @GetMapping("/api/v1/subscription/status")
    String getSubscription(@RequestHeader("X-User-Login") String username);
}
