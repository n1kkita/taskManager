package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public void createGroup(@RequestBody GroupDto group) {
        groupService.create(group);
    }

    @GetMapping("/{id}")
    public GroupEntity getById(@PathVariable Long id){
        return groupService.getById(id);
    }

    @PutMapping("/{idGroup}")
    public void addToGroup(@PathVariable Long idGroup,@RequestBody User addedUser) {
        groupService.addToGroup(idGroup,addedUser);
    }
}
