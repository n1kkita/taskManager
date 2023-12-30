package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.FileEntityDto;
import com.example.taskmanager.models.FileEntity;
import com.example.taskmanager.models.Type;
import com.example.taskmanager.services.interfaceses.TaskFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final TaskFileService taskFileService;
    @PostMapping("/task/{taskId}/set_files")
    public List<FileEntity> setFilesForCreatedTask(@PathVariable Long taskId,
                                       @RequestParam(value = "files",required = false) MultipartFile[] files,
                                       @RequestParam(value = "type_file") Type type){
        return taskFileService.setFilesForTaskById(taskId, files,type);
    }

    @GetMapping("/task/{taskId}/get_files")
    public List<FileEntityDto> getFilesByTask(@PathVariable Long taskId){
        return taskFileService.getFilesByTaskId(taskId);
    }

    @DeleteMapping("/delete")
    @Transactional
    public CompletableFuture<Void> deleteFilesById(@RequestBody List<Long> filesId) {
        return CompletableFuture.runAsync(() -> filesId.forEach(taskFileService::deleteFileById));
    }


    @GetMapping("/download/{fileId}")
    @SneakyThrows
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        FileEntity file = taskFileService.getFileById(fileId);
        Resource resource = new ByteArrayResource(file.getData());
        String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.valueOf(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName)
                .body(resource);
    }

}
