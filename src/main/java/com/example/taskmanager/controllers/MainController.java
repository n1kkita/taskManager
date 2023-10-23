package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.Role;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.interfaceses.GeneralServiceToUserAndGroups;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    @GetMapping("/home/{username}")
    public String showUserPage(@PathVariable String username,Model model,HttpSession session) {
        if(session.getAttribute(username) != null) {
            User user = (User) userService.loadUserByUsername(username);
            List<GroupEntity> otherGroup =
                    user.getGroups().stream()
                            .filter(group -> !group.getOwner().equals(user))
                            .toList();

            model.addAttribute("user",user)
                    .addAttribute("groups",otherGroup);
            return "home";
        } else {
            return "redirect:/registration";
        }
    }
    @GetMapping("/{username}/MyGroups/{idGroup}")
    @SneakyThrows
    public String showMyGroupPage(@PathVariable Long idGroup,
                                  @PathVariable String username,
                                  @RequestParam(name = "page",defaultValue = "0") int page,
                                  HttpSession session,
                                  Model model){

        if(session.getAttribute(username) != null) {
            User user = (User) userService.loadUserByUsername(username);

            GroupEntity groupEntity = user.getOwnGroups()
                    .stream()
                    .filter(group -> group.getId().equals(idGroup))
                    .findAny().orElseThrow();


            List<UserDto> users;
            Page<UserDto> usersPage;
            do {
                usersPage = userService.getAll(PageRequest.of(page, 5));
                users = usersPage.stream()
                        .filter(userInAllStream -> !userInAllStream.getId().equals(user.getId())) //Убираем себя
                        .filter(userInAllStream -> user.getOwnGroups().stream() //Делаем фильтрацию пользователей которых нет в группe
                                .flatMap(group -> group.getUsers().stream())
                                .noneMatch(userInGroup -> userInGroup.getId().equals(userInAllStream.getId())
                                )).toList();
                page++;

            } while (users.isEmpty() && page < usersPage.getTotalPages());

            model.addAttribute("mode", Role.ROLE_ADMIN);
            model.addAttribute("groupId", idGroup);
            model.addAttribute("group", groupEntity);

            model.addAttribute("groupName", groupEntity.getName());
            model.addAttribute("users", users);
            model.addAttribute("currentPage", usersPage.getNumber());
            model.addAttribute("totalPages", usersPage.getTotalPages());

            model.addAttribute("username", user.getEmail())
                    .addAttribute("currentUserId", user.getId())
                    .addAttribute("usersInGroup", groupEntity.getUsers());

            return "calendar";
        } else {
            throw new RuntimeException("");
        }
    }

    @GetMapping("/{username}/otherGroups/{idGroup}")
    public String showHomePage(@PathVariable String username,@PathVariable Long idGroup,Model model,HttpSession session){
        if(session.getAttribute(username) != null) {
            User user = (User) userService.loadUserByUsername(username);

            GroupEntity group = user.getGroups().stream()
                    .filter(group1 -> group1.getId().equals(idGroup))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Группа не найдена"));

            List<UserDto> users = serviceToUserAndGroups.getAllUsersFromGroupById(idGroup);

            model.addAttribute("mode", Role.ROLE_USER)
                    .addAttribute("currentUserId", user.getId())
                    .addAttribute("username", user.getEmail())
                    .addAttribute("groupId", group.getId())
                    .addAttribute("groupName", group.getName())
                    .addAttribute("users", users);

            return "calendar";
        } else {
            throw new RuntimeException("");
        }
    }

}
