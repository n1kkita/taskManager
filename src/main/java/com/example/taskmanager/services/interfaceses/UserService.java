package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.AuthenticationForm;
import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page< User > getAll(Pageable pageable);
    User create(RegistrationForm form);
    User getUserById(Long id);

    User authentication(AuthenticationForm form);
}
