package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.services.interfaceses.GeneralServiceToUserAndGroups;
import com.example.taskmanager.services.interfaceses.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final GeneralServiceToUserAndGroups serviceToUserAndGroups;
    @PostMapping
    public String createGroup(@ModelAttribute GroupDto group) {
        groupService.create(group);
        return "redirect:/home?id="+group.getOwnerId();
    }

    @DeleteMapping("/{idUser}/{idGroup}")
    public ResponseEntity<String> deleteUserFromGroupById(@PathVariable Long idUser, @PathVariable Long idGroup){
        serviceToUserAndGroups.deleteFromGroup(idUser,idGroup);
        return ResponseEntity.ok()
                .body("User with id=" + idUser + " has delete from group with id="+idGroup);
    }
    @PutMapping("/{idGroup}/{addedUserId}")
    public ResponseEntity<String> addToGroup(@PathVariable Long idGroup, @PathVariable Long addedUserId) {
        serviceToUserAndGroups.addToGroup(idGroup, addedUserId);
        return ResponseEntity.ok()
                .body("User with id=" + addedUserId + " has added to group with id="+idGroup);
    }

}
