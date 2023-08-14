package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Task;

import java.util.List;

public interface TaskService {
    List< TaskDto > getAll();
    TaskDto getById(Long id);
    Task saveTask(Task task);
    void deleteById(Long id);
    Task updateTaskById(Long id, Task task);
    void updateTaskStatusById(Long id);

}
