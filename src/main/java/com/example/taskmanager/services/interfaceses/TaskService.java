package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.TaskDto;

import java.util.List;

public interface TaskService {
    List< TaskDto > getAllByGroupId(Long groupId);
    TaskDto saveTask(TaskDto taskDto);
    void deleteById(Long id);
    TaskDto updateTaskById(Long id, TaskDto task);
    TaskDto updateTaskStatusToCompletedById(Long id);

}
