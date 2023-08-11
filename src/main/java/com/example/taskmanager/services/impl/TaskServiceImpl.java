package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.interfaceses.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Override
    @Transactional(readOnly = true)
    public List< TaskDto > getAll() {
        return taskRepository.findAllTasks();
    }
    @Override
    @Transactional(readOnly = true)
    public TaskDto getById(Long id) {
        return taskRepository.findTaskById(id);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void updateTaskById(Long id, Task editTask) {
        taskRepository.findById(id)
                .ifPresent(task -> {
                    task.setTitle(editTask.getTitle());
                    task.setDescription(editTask.getDescription());
                    task.setStatus(editTask.getStatus());
                    task.setDateOfStart(editTask.getDateOfStart());
                    task.setDateOfEnd(editTask.getDateOfEnd());
                    task.setDateOfCreate(LocalDateTime.now());
                });
    }
}
