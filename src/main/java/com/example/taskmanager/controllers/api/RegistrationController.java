package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final HttpSession httpSession;
    @PostMapping
    public void createUser(@RequestBody RegistrationForm form){
        userService.create(form);
        httpSession.setAttribute(form.getEmail(),form.getEmail());
    }
}