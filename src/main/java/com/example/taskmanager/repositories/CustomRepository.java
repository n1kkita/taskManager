package com.example.taskmanager.repositories;

import com.example.taskmanager.models.User;

public interface CustomRepository {
    User findAllFetchGroupsAndTaskAndOwnerGroup();
}
