package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.AuthenticationForm;
import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.models.User;

import java.util.List;

public interface UserService {
    List< User > getAll();
    User create(RegistrationForm form);
    User getUserById(Long id);

    User authentication(AuthenticationForm form);
}
