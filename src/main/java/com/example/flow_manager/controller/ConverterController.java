package com.example.flow_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.flow_manager.model.dto.SaveFileResponse;
import com.example.flow_manager.model.enums.ConversionStatus;
import com.example.flow_manager.service.ConverterService;
import com.example.flow_manager.service.S3Storage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/converter")
public class ConverterController {

    private final ConverterService converterService;
    private final S3Storage s3Storage;

    @PostMapping("/upload")
    public ResponseEntity<SaveFileResponse> saveFile(
        @RequestHeader("X-User-Login") String username,
        @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(s3Storage.saveAndStartConvert(username, file));
    }

    @GetMapping("/download/{*key}")
    public ResponseEntity<byte[]> getFileByFullPath(
        @PathVariable("key") String key
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(s3Storage.getFile(key));
    }

    @GetMapping("/{fileId}/status")
    public ResponseEntity<ConversionStatus> getStatusById(
        @PathVariable Long fileId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(converterService.getStatusById(fileId));
    }
}
