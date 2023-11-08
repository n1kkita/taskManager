package com.example.taskmanager.controllers.api;


import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.events.CreateTaskEvent;
import com.example.taskmanager.events.PerformingTaskWithSendingFile;
import com.example.taskmanager.events.UpdateTaskStatusEvent;
import com.example.taskmanager.events.publisher.EventPublisher;
import com.example.taskmanager.services.interfaceses.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final EventPublisher eventPublisher;
    @GetMapping("/groups/{groupId}")
    public List< TaskDto > getAllTasksByGroupId(@PathVariable Long groupId){
        return taskService.getAllByGroupId(groupId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskDto saveTask(@RequestBody TaskDto taskDto){
        TaskDto savedTaskDto = taskService.saveTask(taskDto);
        eventPublisher.publish(new CreateTaskEvent(savedTaskDto));
        return savedTaskDto;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id, @RequestBody TaskDto task){
        taskService.updateTaskById(id,task);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}")
    public void updateTaskStatus(@PathVariable Long id, @RequestParam(value = "files",required = false) MultipartFile[] files){
        TaskDto taskDto = taskService.updateTaskStatusToCompletedById(id);
        if(files !=null){
            eventPublisher.publish(new PerformingTaskWithSendingFile(taskDto, files));
        } else {
            eventPublisher.publish(new UpdateTaskStatusEvent(taskDto)); //on a first time
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id){
        taskService.deleteById(id);
    }

}
