package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.AuthenticationForm;
import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.Role;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService{
private final UserRepository userRepository;
    @Override
    public Page< UserDto > getAll(Pageable pageable) {
        return userRepository.findAllUserDto(pageable);
    }

    @Override
    public Page< UserDto > searchByLogin(String login,Pageable pageable) {
        return userRepository.searchAllByLogin(login,pageable);
    }

    @Override
    public User create(RegistrationForm form) {

        User user = new User();
        user.setLogin(form.getLogin());
        user.setPassword(form.getPassword());
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findUserFetchGroupsAndTaskById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found"));
    }

    @Override
    public String getLoginById(Long id) {
        return userRepository.findLoginById(id)
                .orElseThrow();
    }

    @Override
    public Long authentication(AuthenticationForm form) {
        return userRepository.findUserIdByLoginAndPassword(form.getLogin(), form.getPassword())
                .orElseThrow(()->new EntityNotFoundException("Логин или пароль не верны"));
    }


}
