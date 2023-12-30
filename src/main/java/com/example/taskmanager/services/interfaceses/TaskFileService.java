package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.FileEntityDto;
import com.example.taskmanager.models.FileEntity;
import com.example.taskmanager.models.Type;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskFileService {
    List<FileEntity> setFilesForTaskById(Long id, MultipartFile[] files, Type type);
    void deleteFileByTaskId(Long id);
    List<FileEntityDto> getFilesByTaskId(Long id);
    FileEntity getFileById(Long id);

    void deleteFileById(Long id);
}
