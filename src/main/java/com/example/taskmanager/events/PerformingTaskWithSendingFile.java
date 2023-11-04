package com.example.taskmanager.events;

import com.example.taskmanager.dto.TaskDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;
@Getter
public class PerformingTaskWithSendingFile extends ApplicationEvent {
    private final MultipartFile[] files;
    public PerformingTaskWithSendingFile(TaskDto taskDto, MultipartFile[] files) {
        super(taskDto);
        this.files = files;
    }
}
