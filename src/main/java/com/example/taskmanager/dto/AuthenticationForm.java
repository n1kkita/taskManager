package com.example.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthenticationForm {
    private String email;
    private String password;
}
