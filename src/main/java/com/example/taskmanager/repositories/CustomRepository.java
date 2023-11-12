package com.example.taskmanager.repositories;

import com.example.taskmanager.models.User;

import java.util.Optional;

public interface CustomRepository {
    Optional<User> findUserFetchOwnGroupById(Long id);

    void deleteGroupById(Long id);
}
