package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List< User > getAll();
    User create(User user);
    User getUserById(Long id);
}
