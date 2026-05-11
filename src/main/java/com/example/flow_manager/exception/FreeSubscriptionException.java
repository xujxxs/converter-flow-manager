package com.example.flow_manager.exception;

public class FreeSubscriptionException extends RuntimeException {

    public FreeSubscriptionException() {
        super("Can't upload file: free subscription");
    }
}
