package com.example.taskmanager.services.impl;

import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {
private final UserRepository userRepository;
    @Override
    public List< User > getAll() {
        return userRepository.findAll();
    }
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not found"));
    }
}
