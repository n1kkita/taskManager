package com.example.taskmanager.repositories;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository< Task, Long > {
    List< TaskDto > findAllTasks();
    TaskDto findTaskById(Long id);
}
