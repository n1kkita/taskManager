package com.example.taskmanager.repositories;

import com.example.taskmanager.models.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
    List<FileEntity> findFileEntitiesByTaskId(Long id);
}
