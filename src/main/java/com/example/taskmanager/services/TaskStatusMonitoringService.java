package com.example.taskmanager.services;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.events.UpdateTaskStatusEvent;
import com.example.taskmanager.events.publisher.EventPublisher;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.impl.task.TaskServiceImpl;
import com.example.taskmanager.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusMonitoringService {
    private final TaskRepository taskRepository;
    private final TaskServiceImpl taskService;
    private final EventPublisher eventPublisher;
    private final Util util;
    @Scheduled(fixedRate = 60000)
    public void monitoringStatusTasks() {
        List<TaskDto> tasks = taskRepository.findAllTasks();
        tasks.forEach(taskDto -> {
            Status status = util.checkStatus(taskDto.getStatus(), taskDto.getDateOfStart(), taskDto.getDateOfEnd());
            if (!status.equals(taskDto.getStatus())) {
                taskService.updateTaskStatusById(taskDto.getId(),status);
                taskDto.setStatus(status);
                eventPublisher.publish(new UpdateTaskStatusEvent(taskDto));
            }
        });
    }
}
