package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.AuthenticationForm;
import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public Page< UserDto > getAll(Pageable pageable){
        return userService.getAll(pageable);
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/login")
    public String getLoginById(@PathVariable Long id){
        return userService.getLoginById(id);
    }

    @PostMapping("/authentication")
    public Long authentication(@RequestBody AuthenticationForm form,HttpSession session){
        Long id = userService.authentication(form);
        session.setAttribute("successfulUserIdFromForm", id);
        return id;
    }

    @PostMapping
    public User createUser(@RequestBody RegistrationForm form, HttpSession session){
        User user = userService.create(form);
        session.setAttribute("user", user);
        return user;
    }

}
