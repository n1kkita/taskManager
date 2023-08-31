package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.services.interfaceses.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
