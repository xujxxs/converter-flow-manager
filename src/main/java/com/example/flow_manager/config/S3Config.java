package com.example.flow_manager.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3ConfigProperties s3ConfigProperties;

    @Bean
    S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
            s3ConfigProperties.getAccessKey(), s3ConfigProperties.getSecretKey());
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        return S3Client.builder()
                .credentialsProvider(provider)
                .endpointOverride(URI.create(s3ConfigProperties.getEndpointUrl()))
                .forcePathStyle(true)
                .region(Region.of(s3ConfigProperties.getRegion()))
            .build();
    }
}
