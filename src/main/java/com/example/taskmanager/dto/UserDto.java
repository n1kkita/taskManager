package com.example.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String login;
}
