package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.Role;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @GetMapping("/home")
    public String showUserPage(HttpSession session, Model model, Pageable pageable){

        Long userId = (Long) session.getAttribute("successfulUserIdFromForm");
        User user;

        if(userId != null) {
            user = userService.getUserById(userId);
        } else {
            return "redirect:/registration";
        }

        User currentUser = user;
        Page< UserDto > usersPage = userService.getAll(pageable);

        List<UserDto> users = usersPage.stream()
                .filter(userInAllStream -> !userInAllStream.getId().equals(currentUser.getId())) //Убираем себя
                .filter(userInAllStream -> currentUser.getOwnGroup().stream() //Делаем фильтрацию пользователей// которых нет в группе
                        .flatMap(group -> group.getUsers().stream())
                        .noneMatch(userInGroup-> userInGroup.getId().equals(userInAllStream.getId())
                        )).toList();

        List<GroupEntity> otherGroup = user.getGroups().stream()
                .filter(group -> ! group.getOwner().equals(currentUser)).toList();

        model.addAttribute("user",user);
        model.addAttribute("users",users);
        model.addAttribute("groups",otherGroup);

        return "home";
    }
    @GetMapping("/MyGroup")
    public String showHomePage(Model model,HttpSession session){
        Long id = (Long) session.getAttribute("successfulUserIdFromForm");
        User user = userService.getUserById(id);
        user.setRole(Role.ROLE_ADMIN);

        model.addAttribute("mode",user.getRole());
        model.addAttribute("groupId",user.getOwnGroup()
                .map(GroupEntity::getId).orElseThrow());

        model.addAttribute("users", user.getOwnGroup()
                .map(GroupEntity::getUsers)
                .orElseThrow());

        return "calendar";
    }

    @GetMapping("/otherGroups/{idGroup}")
    public String showHomePage(@PathVariable Long idGroup, Model model,HttpSession session){
        Long id = (Long) session.getAttribute("successfulUserIdFromForm");
        User user = userService.getUserById(id);
        System.out.println(idGroup);

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


        model.addAttribute("mode",user.getRole());
        model.addAttribute("currentUserId", user.getId());
        model.addAttribute("currentUserLogin", user.getLogin());
        model.addAttribute("groupId",group.getId());
        model.addAttribute("groupName",group.getName());
        model.addAttribute("users", users);

        return "calendar";
    }

}
