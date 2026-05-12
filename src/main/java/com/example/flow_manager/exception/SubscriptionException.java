package com.example.flow_manager.exception;

public class SubscriptionException extends RuntimeException {

    public SubscriptionException() {
        super("Can't upload file: Limit by subscription");
    }
}
