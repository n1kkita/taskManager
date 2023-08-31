package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.services.interfaceses.GeneralServiceToUserAndGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GeneralController {
    private final GeneralServiceToUserAndGroups serviceToUserAndGroups;
    @PutMapping("/groups/{idGroup}/{idUser}/leave")
    public ResponseEntity<String> leavingTheGroup(@PathVariable Long idGroup,@PathVariable Long idUser) {
        serviceToUserAndGroups.leavingTheGroup(idGroup,idUser);
        return ResponseEntity.ok()
                .body(String.valueOf(idUser));
    }
    @DeleteMapping("/groups/{idUser}/{idGroup}")
    public ResponseEntity<String> deleteUserFromGroupById(@PathVariable Long idUser, @PathVariable Long idGroup){
        serviceToUserAndGroups.deleteFromGroup(idUser,idGroup);
        return ResponseEntity.ok()
                .body("User with id=" + idUser + " has delete from group with id="+idGroup);
    }
    @PutMapping("/groups/{idGroup}/{addedUserId}")
    public ResponseEntity<String> addToGroup(@PathVariable Long idGroup, @PathVariable Long addedUserId) {
        serviceToUserAndGroups.addToGroup(idGroup, addedUserId);
        return ResponseEntity.ok()
                .body("User with id=" + addedUserId + " has added to group with id="+idGroup);
    }
    @GetMapping("/users/groups/{idGroup}")
    public List< UserDto > getAllUserFromGroup(@PathVariable Long idGroup){
        return serviceToUserAndGroups.getAllUsersFromGroupById(idGroup);
    }
}
