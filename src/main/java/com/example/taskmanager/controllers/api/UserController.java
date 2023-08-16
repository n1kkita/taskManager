package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.AuthenticationForm;
import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public List< User > getAll(){
        return userService.getAll();
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/authentication")
    public User authentication(@RequestBody AuthenticationForm form,HttpSession session){
        User user = userService.authentication(form);
        session.setAttribute("user", user);
        return user;
    }

    @PostMapping
    public User createUser(@RequestBody RegistrationForm form, HttpSession session){
        User user = userService.create(form);
        session.setAttribute("user", user);
        return user;
    }

}
