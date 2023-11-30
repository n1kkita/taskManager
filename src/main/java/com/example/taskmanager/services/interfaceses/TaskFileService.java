package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.models.FileEntity;
import com.example.taskmanager.models.Task;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskFileService {
    Task setFilesForTaskById(Long id, MultipartFile[] files);
    List<FileEntity> getFilesByTaskId(Long id);
}
