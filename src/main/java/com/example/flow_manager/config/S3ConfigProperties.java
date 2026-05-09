package com.example.flow_manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ConfigurationProperties("s3.properties")
@Component
public class S3ConfigProperties {
    private String endpointUrl;
    private String region;
    private String accessKey;
    private String secretKey;
}
