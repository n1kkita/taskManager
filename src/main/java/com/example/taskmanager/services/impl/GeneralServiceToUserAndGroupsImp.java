package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.GeneralServiceToUserAndGroups;
import com.example.taskmanager.services.interfaceses.GroupService;
import com.example.taskmanager.services.interfaceses.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneralServiceToUserAndGroupsImp implements GeneralServiceToUserAndGroups {
    private final UserService userService;
    private final GroupService groupService;
    @Override
    public List< UserDto > getAllUsersFromGroupById(Long idGroup){
        return groupService.getById(idGroup)
                .getUsers()
                .stream()
                .map(u-> new UserDto(u.getId(),u.getEmail(),u.getName()))
                .toList();
    }


    @Override
    @Transactional
    public void addToGroup(Long idGroup, Long addedUserId) {
        GroupEntity  group = groupService.getById(idGroup);
        group.getUsers().add(userService.getUserById(addedUserId));
    }


    @Override
    @Transactional
    public void deleteFromGroup(Long idUser, Long idGroup) {
        groupService.getById(idGroup).getUsers()
                .removeIf(user -> user.getId().equals(idUser));
    }

    @Override
    @Transactional
    public void leavingTheGroup(Long idGroup, Long idUser) {
        GroupEntity group = groupService.getById(idGroup);
        User user = userService.getUserById(idUser);

        group.getUsers().remove(user);
    }

    @Override
    @Transactional
    public void addOwnerToGroup(Long idGroup, Long ownerId, Long newOwnerId) {
        User currentOwner = userService.getUserById(ownerId);
        User newOwner = userService.getUserById(newOwnerId);
        GroupEntity group = currentOwner
                .getOwnGroups()
                .stream()
                .filter(g -> g.getId().equals(idGroup)).findAny().orElseThrow();

        group.addOwnerToGroup(newOwner);
    }
}
