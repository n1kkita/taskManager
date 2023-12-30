package com.example.taskmanager.dto;

import com.example.taskmanager.models.Type;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileEntityDto {
    private Long id;
    private String name;
    private Type type;
}
