package com.example.taskmanager.events;

import com.example.taskmanager.dto.TaskDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
@Getter
public class PerformingTaskWithSendingFile extends ApplicationEvent {
    private final MultipartFile file;
    public PerformingTaskWithSendingFile(TaskDto taskDto, MultipartFile file) {
        super(taskDto);
        this.file = file;
    }
}
