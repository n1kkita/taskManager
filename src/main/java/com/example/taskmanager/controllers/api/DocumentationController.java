package com.example.taskmanager.controllers.api;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentationController {

    @GetMapping(value = "/documentation")
    @SneakyThrows
    public ResponseEntity<Resource> getDocumentation(){
        Resource resource = new ClassPathResource("static/documentation.md");
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .contentType(MediaType.TEXT_MARKDOWN)
                .body(resource);
    }
}
