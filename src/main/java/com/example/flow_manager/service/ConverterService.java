package com.example.flow_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.flow_manager.exception.NotFoundException;
import com.example.flow_manager.model.entity.ConvertedFile;
import com.example.flow_manager.model.entity.FileToConvert;
import com.example.flow_manager.model.entity.FileToConvertOutbox;
import com.example.flow_manager.model.enums.ConversionStatus;
import com.example.flow_manager.model.enums.FileToConvertOutboxStatus;
import com.example.flow_manager.repository.FileToConvertOutboxRepository;
import com.example.flow_manager.repository.FileToConvertRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConverterService {

    private final FileToConvertOutboxRepository fileToConvertOutboxRepository;
    private final FileToConvertRepository fileToConvertRepository;

    public FileToConvert findById(Long fileId) {
        return fileToConvertRepository.findById(fileId)
            .orElseThrow(() -> {
                log.error("Not found file to convert by id: {}", fileId);
                return new NotFoundException("File to convert not found");
            });
    }

    public ConversionStatus getStatusById(Long fileId) {
        return fileToConvertRepository.findById(fileId)
            .orElseThrow(() -> new NotFoundException("File not found")).getStatus();
    }

    @Transactional
    public Long startConvert(String fileFullPath) {
        FileToConvert file = fileToConvertRepository.save(
            FileToConvert.builder()
                    .fullPathS3(fileFullPath)
                    .status(ConversionStatus.IN_PROCESS)
                    .uploadedAt(LocalDateTime.now())
                .build());

        fileToConvertOutboxRepository.save(
            FileToConvertOutbox.builder()
                    .idempotentKey(file.getId().toString())
                    .payload(file.getFullPathS3())
                    .occurredAt(LocalDateTime.now())
                    .status(FileToConvertOutboxStatus.CREATED)
                .build());

        return file.getId();
    }

    public FileToConvert linkConvertedFiles(
        Long idFile2Convert, List<String> convertedFilesKeys
    ) {
        FileToConvert fileToConvert = findById(idFile2Convert);
        Set<ConvertedFile> convertedFiles = convertedFilesKeys.stream()
            .map(key -> ConvertedFile.builder()
                    .fullPathS3(key)
                    .convertedFromFile(fileToConvert)
                    .uploadedAt(LocalDateTime.now())
                .build())
            .collect(Collectors.toSet());
        fileToConvert.setConvertedFiles(convertedFiles);

        return fileToConvertRepository.save(fileToConvert);
    }

    public void updateStatusById(FileToConvert fileToConvert, ConversionStatus status) {
        fileToConvert.setStatus(status);
        fileToConvert.setUpdatedAt(LocalDateTime.now());
        fileToConvertRepository.save(fileToConvert);
    }
}
