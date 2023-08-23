package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.services.interfaceses.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public String createGroup(@ModelAttribute GroupDto group) {
        groupService.create(group);
        return "redirect:/home?id="+group.getOwnerId();
    }

    @GetMapping("/{id}")
    public @ResponseBody GroupEntity getById(@PathVariable Long id){
        return groupService.getById(id);
    }

    @PutMapping("/{idGroup}/{addedUserId}")
    public String addToGroup(@PathVariable Long idGroup, @PathVariable Long addedUserId) {
        groupService.addToGroup(idGroup, addedUserId);
        return "redirect:/home?id="+idGroup;
    }

}
