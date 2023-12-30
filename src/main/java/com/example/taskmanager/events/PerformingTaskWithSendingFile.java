package com.example.taskmanager.events;

import com.example.taskmanager.models.Task;
import org.springframework.context.ApplicationEvent;
public class PerformingTaskWithSendingFile extends ApplicationEvent {
    public PerformingTaskWithSendingFile(Task task) {
        super(task);
    }
}
