package com.example.flow_manager.model.entity;

import java.time.LocalDateTime;

import com.example.flow_manager.model.enums.FileToConvertOutboxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_to_convert_outbox")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileToConvertOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "idempotent_key", nullable = false, unique = true)
    private String idempotentKey;
    
    @Column(name = "payload", nullable = false)
    private String payload;
    
    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;
    
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FileToConvertOutboxStatus status;

    @Override
    public final boolean equals(Object anObject) {
        if(this == anObject) return true;
        if(anObject == null || getClass() != anObject.getClass()) return false;

        return ((FileToConvertOutbox) anObject).getIdempotentKey().equals(this.idempotentKey);
    }

    @Override
    public int hashCode() {
        return this.idempotentKey.hashCode();
    }
}
