package com.example.flow_manager.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.flow_manager.config.S3BucketProperties;
import com.example.flow_manager.exception.FreeSubscriptionException;
import com.example.flow_manager.exception.NotFoundException;
import com.example.flow_manager.exception.S3Exception;
import com.example.flow_manager.model.dto.SaveFileResponse;
import com.example.flow_manager.request.SubscriptionRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Storage {

    @Value("${limits.upload-file.free}")
    private Long fileSizeFree;
    private final SubscriptionRequest subscriptionRequest;
    private final S3BucketProperties s3BucketProperties;
    private final ConverterService converterService;
    private final S3Client s3Client;

    public byte[] getFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3BucketProperties.getConvertedFileBN())
                .key(key)
            .build();

        try {
            return s3Client.getObject(request).readAllBytes();
        } catch(NoSuchKeyException ex) {
            log.warn("No key: {} in S3", key);
            throw new NotFoundException("File not found");
        } catch(IOException ex) {
            log.error("Error with S3. Caused by: ", ex);
            throw new S3Exception();
        }
    }

    public SaveFileResponse saveAndStartConvert(String username, MultipartFile file) {
        if(subscriptionRequest.getSubscription(username).replace("\"", "").equals("FREE") 
            && file.getSize() > fileSizeFree) throw new FreeSubscriptionException();

        String fileFullPath = file.getOriginalFilename();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(s3BucketProperties.getFileToConvertBN())
                .key(fileFullPath)
            .build();
        
        try {
            RequestBody body = RequestBody.fromBytes(file.getBytes());
            s3Client.putObject(request, body);
            log.info("File with key: {}, uploaded to S3 in bucket: {}", 
                fileFullPath, s3BucketProperties.getFileToConvertBN());
            
            Long fileId = converterService.startConvert(fileFullPath);
            return new SaveFileResponse(fileId, fileFullPath);
        } catch(IOException ex) {
            log.error("Error with S3. Caused by: ", ex);
            throw new S3Exception();
        }
    }
}
