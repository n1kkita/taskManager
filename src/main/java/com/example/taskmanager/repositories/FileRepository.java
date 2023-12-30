package com.example.taskmanager.repositories;

import com.example.taskmanager.dto.FileEntityDto;
import com.example.taskmanager.models.FileEntity;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
    @Query("select new com.example.taskmanager.dto.FileEntityDto(f.id,f.name,f.type) from FileEntity f where f.task.id=?1")
    List<FileEntityDto> findFileEntitiesByTaskId(Long id);
    void deleteFileEntityByTaskId(Long id);

}
