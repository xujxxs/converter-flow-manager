package com.example.flow_manager.service.scheduledJob;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.flow_manager.event.Producer;
import com.example.flow_manager.model.entity.FileToConvertOutbox;
import com.example.flow_manager.model.enums.FileToConvertOutboxStatus;
import com.example.flow_manager.repository.FileToConvertOutboxRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxConverterScheduler {

    @Value("${queue.kafka.topic.start-convert}")
    private String START_CONVERT_TOPIC;

    private final Producer producer;
    private final FileToConvertOutboxRepository fileToConvertOutboxRepository;

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void proccessCreated() {
        List<FileToConvertOutbox> events = fileToConvertOutboxRepository.findByStatusOrderByOccurredAtAsc(
            FileToConvertOutboxStatus.CREATED, PageRequest.of(0, 100));
        log.debug("Founded: {} events", events.size());

        if(events.isEmpty()){
            return;
        }

        List<CompletableFuture<Boolean>> futures = events.stream()
            .map(event -> producer
                .sendMessage(START_CONVERT_TOPIC, event.getIdempotentKey(), event.getPayload())
                .thenApply(result -> {
                    event.setStatus(FileToConvertOutboxStatus.PROCESSED);
                    event.setProcessedAt(LocalDateTime.now());
                    return true;
                })
                .exceptionally(ex -> false)) // Можно ещё добавить апдейт статуса на ошибочный и обрабатывать уже в другом методе, где время на ретрай больше
            .toList();
            
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        fileToConvertOutboxRepository.saveAll(events);
    }
}
