package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.Role;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    @GetMapping("/home")
    public String showUserPage(HttpSession session, Model model, Pageable pageable){

        User user = (User) session.getAttribute("user");

        if(user != null) {
            user = userService.getUserById(user.getId());
        } else {
            return "redirect:/registration";
        }

        User finalUser = user;
        model.addAttribute("user",user);


        model.addAttribute("users",
                userService.getAll(pageable).stream()
                        .filter(user1 -> !user1.equals(finalUser))
                        .filter(user1 -> user1.getOwnGroup().isPresent())
        );

        model.addAttribute("groups", user.getGroups().stream()
                .filter(group -> ! group.getOwner().equals(finalUser))
        );

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
        user.setRole(Role.ROLE_ADMIN);
        //Добавить обработку на null
        System.out.println(user.getRole());

        model.addAttribute("mode",user.getRole());
        model.addAttribute("groupId",user.getOwnGroup()
                .map(GroupEntity::getId).orElseThrow());

        model.addAttribute("users", user.getOwnGroup()
                .map(GroupEntity::getUsers)
                .orElseThrow());

        return "home";
    }

    @GetMapping("/otherGroups/{idGroup}")
    public String showHomePage(@PathVariable Long idGroup, Model model,HttpSession session){
        User user = (User) session.getAttribute("user");

        GroupEntity group = user.getGroups().stream()
                .filter(group1 -> group1.getId().equals(idGroup))
                .findFirst()
                .orElseThrow(()-> new EntityNotFoundException("Группа не найдена"));

        List<User> users = user.getGroups()
                .stream()
                .filter(group1 -> group1.getId().equals(idGroup))
                .flatMap(group1 -> group1.getUsers().stream())
                .toList();

        user.setRole(Role.ROLE_USER);

        System.out.println(user.getRole());

        model.addAttribute("mode",user.getRole());
        model.addAttribute("groupId",group.getId());
        model.addAttribute("users", users);

        return "home";
    }

}
