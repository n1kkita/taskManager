package com.example.taskmanager.services.impl;

import com.example.taskmanager.models.FileEntity;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repositories.FileRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.interfaceses.TaskFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskFileServiceImpl implements TaskFileService {
    private final TaskRepository taskRepository;
    private final FileRepository fileRepository;
    @Override
    @SneakyThrows
    @Transactional
    public Task setFilesForTaskById(Long id, MultipartFile[] files)  {
        Task task = taskRepository.findById(id).orElseThrow();
        for (MultipartFile file: files) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setData(file.getBytes());
            fileEntity.setName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            task.setFile(fileEntity);
            fileRepository.save(fileEntity);

        }
        return task;
    }

    @Override
    @Transactional
    public List<FileEntity> getFilesByTaskId(Long id) {
        return fileRepository.findFileEntitiesByTaskId(id);
    }
}
