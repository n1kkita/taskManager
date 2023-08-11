package com.example.taskmanager.controllers;


import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.services.interfaceses.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @GetMapping
    public List< TaskDto > getAllTasks(){
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public List< TaskDto > getTaskById(@PathVariable Long id){
        return taskService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Task saveTask(@RequestBody Task task){
        return taskService.saveTask(task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id){
        taskService.deleteById(id);
    }

}
