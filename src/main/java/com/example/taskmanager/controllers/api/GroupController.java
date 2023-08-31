package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.GroupDto;
import com.example.taskmanager.services.interfaceses.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody GroupDto group) {
        groupService.create(group);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.valueOf(group.getOwnerId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id) {
        groupService.deleteById(id);
        return ResponseEntity.ok()
                .body(String.valueOf(id));
    }
}
