package com.example.taskmanager.controllers;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.exception.NoAuthenticationUser;
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
    public String showRegistrationForm(@RequestParam(required = false) String oauthEmail,Model model) {
        model.addAttribute("registrationForm", new RegistrationForm())
                .addAttribute("email",oauthEmail);
        return "registration";
    }
    @GetMapping("/authentication")
    public String showAuthenticationForm(Model model,@RequestParam(required = false) String error) {
        model.addAttribute("authenticationForm", new RegistrationForm());
        model.addAttribute("errorMessage",error);
        return "authentication";
    }
    @GetMapping("/home/{username}")
    public String showUserPage(@PathVariable String username,Model model,HttpSession session) {
        if(session.getAttribute(username) != null) {
            User user = (User) userService.loadUserByUsername(username);
            List<GroupEntity> otherGroup = user
                            .getGroups().stream()
                            .filter(group -> !group.getOwners().contains(user))
                            .toList();

            model.addAttribute("user",user)
                    .addAttribute("groups",otherGroup);
            return "home";
        } else {
            throw new NoAuthenticationUser();
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

            GroupEntity group =
                     user.getOwnGroups()
                    .stream()
                    .filter(group1 -> group1.getId().equals(idGroup))
                    .findAny().orElseThrow();

            List<User> owners = group.getOwners();
            List<User> usersWithoutOwners = group.getUsers().stream().filter(u->!owners.contains(u)).toList();
            Page<UserDto> pages =  userService.searchByLogin("", idGroup, PageRequest.of(page,5));
            List<UserDto> users = pages.getContent();


            model.addAttribute("mode", Role.ROLE_ADMIN)
                    .addAttribute("groupId", idGroup)
                    .addAttribute("group", group)
                    .addAttribute("groupName", group.getName())
                    .addAttribute("users", users)
                    .addAttribute("currentPage", pages.getNumber())
                    .addAttribute("totalPages", pages.getTotalPages())
                    .addAttribute("username", user.getEmail())
                    .addAttribute("currentUserId", user.getId())
                    .addAttribute("usersInGroupWithoutOwners", usersWithoutOwners)
                    .addAttribute("usersInGroup", group.getUsers());

            return "calendar";

        } else {
            throw new NoAuthenticationUser();
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
            throw new NoAuthenticationUser();
        }
    }

}
