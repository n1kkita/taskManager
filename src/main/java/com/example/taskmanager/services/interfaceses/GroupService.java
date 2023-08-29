package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.models.GroupEntity;

public interface GroupService {
    GroupEntity create(GroupDto group);
    GroupEntity getById(Long id);

}
