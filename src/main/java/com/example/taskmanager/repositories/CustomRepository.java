package com.example.taskmanager.repositories;

import com.example.taskmanager.models.User;

import java.util.List;
import java.util.Optional;

public interface CustomRepository {
    Optional<User> findUserFetchGroupsAndTaskById(Long id);
}
