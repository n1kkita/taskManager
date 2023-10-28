package com.example.taskmanager.events;

import com.example.taskmanager.models.Task;
import org.springframework.context.ApplicationEvent;

public class CreateTaskEvent extends ApplicationEvent {
    public CreateTaskEvent(Task source) {
        super(source);
    }
}
