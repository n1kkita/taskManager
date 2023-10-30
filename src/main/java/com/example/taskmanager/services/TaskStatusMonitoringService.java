package com.example.taskmanager.services;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.events.UpdateTaskStatusEvent;
import com.example.taskmanager.events.publisher.EventPublisher;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskStatusMonitoringService {
    private final TaskRepository taskRepository;
    private final EventPublisher eventPublisher;
    private final Util util; 

    @Scheduled(fixedRate = 60000)
    public void monitoringStatusTasks() {
        Page<TaskDto> tasks = taskRepository.findAllTasks(PageRequest.ofSize(10));
        tasks.stream().forEach(taskDto -> {
            Status status = util.checkStatus(taskDto.getStatus(), taskDto.getDateOfStart(), taskDto.getDateOfEnd(), taskDto.getId());

            if (!status.equals(taskDto.getStatus())) {
                taskDto.setStatus(status);
                eventPublisher.publish(new UpdateTaskStatusEvent(taskDto));
            }
        });
    }
}
