package com.example.flow_manager.service.consumer;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.example.flow_manager.model.entity.FileToConvert;
import com.example.flow_manager.model.enums.ConversionStatus;
import com.example.flow_manager.service.ConverterService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConverterConsumer {

    private final ConverterService converterService;

    @KafkaListener(topics = "${queue.kafka.topic.end-convert.success}", groupId = "${queue.kafka.group-id}")
    public void waitSuccessEndConvert(List<String> event, @Header(KafkaHeaders.RECEIVED_KEY) @NonNull String key) {
        log.info("Converting end success: {}, converted files: {}", key, event);

        if(event.isEmpty()) {
            converterService.updateStatusById(
                converterService.findById(Long.valueOf(key)), 
                ConversionStatus.NOT_SUPPORT);
            return;
        }

        FileToConvert file2Convert = converterService.linkConvertedFiles(Long.valueOf(key), event);
        converterService.updateStatusById(file2Convert, ConversionStatus.SUCCESS);
    }

    @KafkaListener(topics = "${queue.kafka.topic.end-convert.error}", groupId = "${queue.kafka.group-id}")
    public void waitErrorEndConvert(String event, @Header(KafkaHeaders.RECEIVED_KEY) @NonNull String key) {
        log.info("Converting end error: {}", event);
        
        converterService.updateStatusById(
            converterService.findById(Long.valueOf(key)), 
            ConversionStatus.ERROR);
    }
}
