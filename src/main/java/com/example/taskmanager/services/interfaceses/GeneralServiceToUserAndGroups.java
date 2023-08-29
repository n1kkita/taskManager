package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.UserDto;

import java.util.List;

public interface GeneralServiceToUserAndGroups {
    List< UserDto > getAllUsersFromGroupById(Long idGroup);

    void addToGroup(Long idGroup, Long addedUserId);

     void deleteFromGroup(Long idUser, Long idGroup);

}
