package com.example.taskmanager.controllers.api;

import com.example.taskmanager.events.CreateTaskEvent;
import com.example.taskmanager.events.PerformingTaskWithSendingFile;
import com.example.taskmanager.events.publisher.EventPublisher;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final TaskRepository taskRepository;
    private final EventPublisher eventPublisher;
    @PostMapping("/create_task/{taskId}")
    public void sendMessageWhenCreateTask(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        eventPublisher.publish(new CreateTaskEvent(task));
    }
    @PostMapping("/completed_task/{taskId}")
    public void sendMessageWhenTaskCompleted(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        eventPublisher.publish(new PerformingTaskWithSendingFile(task));
    }

}
