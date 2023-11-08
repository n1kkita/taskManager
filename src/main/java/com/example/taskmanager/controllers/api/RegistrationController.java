package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String createUser(@ModelAttribute(name = "form") @Valid RegistrationForm form, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors())
            return "registration";

        userService.create(form);
        model.addAttribute("registrationForm",form);
        return "/authentication";
    }
}
