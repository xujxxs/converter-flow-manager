package com.example.flow_manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties("s3.buckets")
@Component
public class S3BucketProperties {
    private String convertedFileBN;
    private String fileToConvertBN;
}
