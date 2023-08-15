package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.User;

public interface GroupService {
    GroupEntity create(GroupDto group);

    void addToGroup(Long idGroup, User addedUser);

    GroupEntity getById(Long id);
}
