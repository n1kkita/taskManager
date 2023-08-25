package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.Role;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.UserService;
import com.example.taskmanager.utils.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showUserPage(@RequestParam Long id,
                               HttpSession session,
                               Model model,
                               Pageable pageable) throws JsonProcessingException {

        Long userId = (Long) session.getAttribute(Util.replaceToUserLinkInHttpSession(id));
        User user;

        if(userId != null) {
            user = userService.getUserById(userId);
        } else {
            return "redirect:/registration";
        }

        User currentUser = user;
        Page< UserDto > usersPage = userService.getAll(pageable);

        List<UserDto> userss = usersPage.stream()
                .filter(userInAllStream -> !userInAllStream.getId().equals(currentUser.getId())) //Убираем себя
                .filter(userInAllStream -> currentUser.getOwnGroup().stream() //Делаем фильтрацию пользователей которых нет в группe
                        .flatMap(group -> group.getUsers().stream())
                        .noneMatch(userInGroup-> userInGroup.getId().equals(userInAllStream.getId())
                        )).toList();

        List<GroupEntity> otherGroup = user.getGroups().stream()
                .filter(group -> ! group.getOwner().equals(currentUser)).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        String users = objectMapper.writeValueAsString(userss);

        model.addAttribute("user",user)
                .addAttribute("users",users)
                .addAttribute("groups",otherGroup);



        return "home";
    }
    @GetMapping("/MyGroup")
    public String showMyGroupPage(@RequestParam Long idUser, Model model, HttpSession session){
        Long id = (Long) session.getAttribute(Util.replaceToUserLinkInHttpSession(idUser));
        User user = userService.getUserById(id);
        user.setRole(Role.ROLE_ADMIN);

        model.addAttribute("mode",user.getRole());
        model.addAttribute("groupId",user.getOwnGroup()
                .map(GroupEntity::getId).orElseThrow());

        model.addAttribute("groupName", user.getOwnGroup()
                .map(GroupEntity::getName)
                .orElseThrow());

        model.addAttribute("currentUserLogin", user.getLogin())
                .addAttribute("currentUserId", user.getId())
                .addAttribute("users", user.getOwnGroup()
                .map(GroupEntity::getUsers)
                .orElseThrow());

        return "calendar";
    }

    @GetMapping("/otherGroups/{idGroup}")
    public String showHomePage(@PathVariable Long idGroup,@RequestParam Long idUser, Model model,HttpSession session){
        Long id = (Long) session.getAttribute(Util.replaceToUserLinkInHttpSession(idUser));
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


        model.addAttribute("mode",user.getRole())
                .addAttribute("currentUserId", user.getId())
                .addAttribute("currentUserLogin", user.getLogin())
                .addAttribute("groupId",group.getId())
                .addAttribute("groupName",group.getName())
                .addAttribute("users", users);

        return "calendar";
    }

}
