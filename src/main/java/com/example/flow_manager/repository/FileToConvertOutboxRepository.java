package com.example.flow_manager.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.flow_manager.model.entity.FileToConvertOutbox;
import com.example.flow_manager.model.enums.FileToConvertOutboxStatus;

public interface FileToConvertOutboxRepository extends JpaRepository<FileToConvertOutbox, Long> {

    List<FileToConvertOutbox> findByStatusOrderByOccurredAtAsc(
        FileToConvertOutboxStatus status, Pageable pageable);
}
