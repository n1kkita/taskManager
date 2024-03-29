package com.example.taskmanager.controllers.api;


import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.events.CreateTaskEvent;
import com.example.taskmanager.events.PerformingTaskWithSendingFile;
import com.example.taskmanager.events.UpdateTaskStatusEvent;
import com.example.taskmanager.events.publisher.EventPublisher;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.services.interfaceses.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/groups/{groupId}")
    public List< TaskDto > getAllTasksByGroupId(@PathVariable Long groupId){
        return taskService.getAllByGroupId(groupId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskDto saveTask(@RequestBody TaskDto taskDto){
        return taskService.saveTask(taskDto);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto task){
        TaskDto taskDto = taskService.updateTaskById(id, task);
        System.out.println(taskDto);
        return taskDto;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}")
    public TaskDto updateTaskStatus(@PathVariable Long id){
        return taskService.updateTaskStatusToCompletedById(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id){
        taskService.deleteById(id);
        return ResponseEntity.ok()
                .header("deleteTaskId", String.valueOf(id))
                .build();
    }

}
