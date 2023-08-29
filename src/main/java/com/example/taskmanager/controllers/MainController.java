package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.Role;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.GeneralServiceToUserAndGroups;
import com.example.taskmanager.services.interfaceses.UserService;
import com.example.taskmanager.utils.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final GeneralServiceToUserAndGroups serviceToUserAndGroups;

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
                               @RequestParam(name = "page",defaultValue = "0") int page,
                               HttpSession session,
                               Model model) throws JsonProcessingException {

        Long userId = (Long) session.getAttribute(Util.replaceToUserLinkInHttpSession(id)); //получаем id
        User currentUser;

        if(userId != null) {
            currentUser = userService.getUserById(userId); //находим в базе
        } else {
            return "redirect:/registration";
        }

        List<UserDto> users;
        Page< UserDto > usersPage;
        do {
            usersPage = userService.getAll(PageRequest.of(page, 10));
            users = usersPage.stream()
                    .filter(userInAllStream -> !userInAllStream.getId().equals(currentUser.getId())) //Убираем себя
                    .filter(userInAllStream -> currentUser.getOwnGroup().stream() //Делаем фильтрацию пользователей которых нет в группe
                            .flatMap(group -> group.getUsers().stream())
                            .noneMatch(userInGroup-> userInGroup.getId().equals(userInAllStream.getId())
                            )).toList();
            page++;

        } while (users.isEmpty() && page < usersPage.getTotalPages());

        List<GroupEntity> otherGroup = currentUser.getGroups().stream()
                .filter(group -> ! group.getOwner().equals(currentUser)).toList();

        ObjectMapper objectMapper = new ObjectMapper();
        String usersJSON = objectMapper.writeValueAsString(users); //преобразуем в json

        model.addAttribute("user",currentUser)
                .addAttribute("users",users)
                .addAttribute("groups",otherGroup)
                .addAttribute("usersJSON",usersJSON)
                .addAttribute("currentPage", usersPage.getNumber())
                .addAttribute("totalPages", usersPage.getTotalPages());



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
        user.setRole(Role.ROLE_USER);

        GroupEntity group = user.getGroups().stream()
                .filter(group1 -> group1.getId().equals(idGroup))
                .findFirst()
                .orElseThrow(()-> new EntityNotFoundException("Группа не найдена"));

        List< UserDto > users = serviceToUserAndGroups.getAllUsersFromGroupById(idGroup);

        model.addAttribute("mode",user.getRole())
                .addAttribute("currentUserId", user.getId())
                .addAttribute("currentUserLogin", user.getLogin())
                .addAttribute("groupId",group.getId())
                .addAttribute("groupName",group.getName())
                .addAttribute("users", users);

        return "calendar";
    }

}
