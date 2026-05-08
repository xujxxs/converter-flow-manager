package com.example.flow_manager.exception;

public class S3Exception extends RuntimeException {

    public S3Exception() {
        super("Error with s3");
    }


    public S3Exception(Throwable throwable) {
        super("Error with s3 by: ", throwable);
    }
}
