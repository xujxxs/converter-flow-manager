package com.example.flow_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.flow_manager.model.entity.ConvertedFile;

public interface ConvertedFileRepository extends JpaRepository<ConvertedFile, Long> {

}
