package com.example.taskmanager.events;

import com.example.taskmanager.dto.TaskDto;
import org.springframework.context.ApplicationEvent;

public class UpdateTaskStatusEvent extends ApplicationEvent {
    public UpdateTaskStatusEvent(TaskDto source) {
        super(source);
    }
}
