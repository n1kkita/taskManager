package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.GroupRepository;
import com.example.taskmanager.services.interfaceses.GroupService;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImp implements GroupService {
    private final UserService userService;
    private final GroupRepository groupRepository;
    public GroupEntity create(GroupDto group) {
        User user = userService.getUserById(group.getOwnerId());
        String groupName = group.getName();

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(groupName);

        groupEntity.setOwner(user);

        return groupRepository.save(groupEntity);
    }

    @Override
    public void addToGroup(Long idGroup, Long addedUserId) {
        Optional< GroupEntity > groupEntity = groupRepository.findById(idGroup);
        groupEntity.ifPresent(group -> group.getUsers().add(userService.getUserById(addedUserId)));
    }
    @Override
    public GroupEntity getById(Long id) {
        return groupRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Not found with id:" + id));
    }
}
