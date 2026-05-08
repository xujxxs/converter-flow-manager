package com.example.flow_manager.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable thr) {
        super(message, thr);
    }
}
