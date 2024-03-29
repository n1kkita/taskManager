package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.AuthenticationForm;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {
    private final UserService userService;
    @PostMapping
    public void authentication(@ModelAttribute AuthenticationForm form){
        userService.loadUserByUsername(form.getEmail());
    }
}
