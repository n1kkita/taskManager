package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.GroupService;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final GroupService groupService;
    @GetMapping("/home")
    public String showUserPage(HttpSession session,Model model){

        User user = (User) session.getAttribute("user");

        if(user != null) {
            user = userService.getUserById(user.getId());
        } else {
            return "redirect:/registration";
        }
        model.addAttribute("user",user);
        model.addAttribute("groups", user.getGroups());
        return "userPage";
    }
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "registration";
    }

    @GetMapping("/authentication")
    public String showAuthenticationForm(Model model) {
        model.addAttribute("authenticationForm", new RegistrationForm());
        return "authentication";
    }

    @GetMapping("/MyGroup")
    public String showHomePage(Model model,HttpSession session){
        User user = (User) session.getAttribute("user");

        user = userService.getUserById(user.getId());
        //Добавить обработку на null

        model.addAttribute("groupId",user.getOwnGroup().getId());
        model.addAttribute("users", user.getOwnGroup().getUsers().stream().toList());
        return "home";
    }

    @GetMapping("/groups/{idGroup}")
    public String showHomePage(@PathVariable Long idGroup, Model model,HttpSession session){
        User user = (User) session.getAttribute("user");

        model.addAttribute("groupId",user.getGroups().stream()
                .filter(group -> group.getId().equals(idGroup))
                .findFirst()
                .orElseThrow(()-> new EntityNotFoundException("Группа не найдена"))
        );
        return "home";
    }

}
