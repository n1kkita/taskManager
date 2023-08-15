package com.example.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class UserDto {
    private String login;
    private String ownerGroup;
}
