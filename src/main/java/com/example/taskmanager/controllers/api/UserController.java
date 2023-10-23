package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public Page< UserDto > getAll(Pageable pageable){
        return userService.getAll(pageable);
    }
    @GetMapping("/search")
    public Page< UserDto > getAll(@RequestParam String login,@RequestParam Long idGroup){
        return userService.searchByLogin(login, idGroup, PageRequest.of(0,5));
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    @GetMapping("/{id}/login")
    public String getLoginById(@PathVariable Long id){
        return userService.getLoginById(id);
    }
}
