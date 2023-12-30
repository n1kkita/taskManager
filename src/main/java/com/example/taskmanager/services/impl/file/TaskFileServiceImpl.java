package com.example.taskmanager.services.impl.file;

import com.example.taskmanager.dto.FileEntityDto;
import com.example.taskmanager.models.FileEntity;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.models.Type;
import com.example.taskmanager.repositories.FileRepository;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.interfaceses.TaskFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskFileServiceImpl implements TaskFileService {
    private final TaskRepository taskRepository;
    private final FileRepository fileRepository;
    @Override
    @SneakyThrows
    @Transactional
    public List<FileEntity> setFilesForTaskById(Long id, MultipartFile[] files, Type type)  {
        Task task = taskRepository.findById(id).orElseThrow();
        List<FileEntity> fileEntities = new ArrayList<>();
        if(files != null) {
            for (MultipartFile file : files) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setData(file.getBytes());
                fileEntity.setName(file.getOriginalFilename());
                fileEntity.setContentType(file.getContentType());
                fileEntity.setType(type);
                task.setFile(fileEntity);

                FileEntity savedFileEntity = fileRepository.save(fileEntity);
                fileEntities.add(savedFileEntity);
            }
        }
        return fileEntities;
    }

    @Override
    @Transactional
    public void deleteFileByTaskId(Long id) {
        fileRepository.deleteFileEntityByTaskId(id);
    }

    @Override
    @Transactional
    public List<FileEntityDto> getFilesByTaskId(Long id) {
        return fileRepository.findFileEntitiesByTaskId(id);
    }

    @Override
    @Transactional
    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void deleteFileById(Long id) {
        fileRepository.deleteById(id);
    }
}
