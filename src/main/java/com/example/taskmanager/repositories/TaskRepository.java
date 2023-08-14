package com.example.taskmanager.repositories;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository< Task, Long > {


    @Query("select new com.example.taskmanager.dto.TaskDto(t.id,t.title,t.description,t.status,t.dateOfEnd,t.dateOfStart,t.allDay) from Task t where t.id = ?1")
    Optional<TaskDto> findTaskById(Long id);
}
