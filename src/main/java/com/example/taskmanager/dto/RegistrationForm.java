package com.example.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationForm {
    @Size(min = 3,max = 12,message = "Кількість символів імя має бути мінімум 3")
    private String name;
    @Email(message = "Введіть правильно електрону пошту")
    private String email;
    @Size(min =8,max = 23 ,message = "Кількість символів пароля має бути мінімум 8")
    private String password;
}
