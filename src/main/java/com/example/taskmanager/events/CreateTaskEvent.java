package com.example.taskmanager.events;

import com.example.taskmanager.dto.TaskDto;
import org.springframework.context.ApplicationEvent;

public class CreateTaskEvent extends ApplicationEvent {
    public CreateTaskEvent(TaskDto taskDto) {
        super(taskDto);
    }
}
