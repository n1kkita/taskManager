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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/registration")
    public String showRegistrationForm(@RequestParam(required = false) String oauthEmail,
                                       @ModelAttribute(name = "form") RegistrationForm registrationForm,
                                       Model model) {
        model.addAttribute("oauthEmail",oauthEmail);
        return "registration";
    }
    @GetMapping("/authentication")
    public String showAuthenticationForm(Model model,@RequestParam(required = false) String error) {
        model.addAttribute("authenticationForm", new RegistrationForm());
        model.addAttribute("errorMessage",error);
        return "authentication";
    }
    @GetMapping("/home/{username}")
    public String home(@PathVariable String username,Model model,HttpSession session) {
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
    @GetMapping("/{username}/groups/{idGroup}")
    @PreAuthorize("#username == authentication.principal.username")
    @SneakyThrows
    public String MyGroup(@PathVariable Long idGroup,
                          @PathVariable String username,
                          @RequestParam(name = "page",defaultValue = "0") int page,
                          @RequestParam(name = "autoOpenTask",required = false) boolean autoOpenTask,
                          @RequestParam(name = "autoOpenTaskId",required = false) Integer autoOpenTaskId,
                          HttpSession session,
                          Model model){

        if(session.getAttribute(username) != null || autoOpenTask) {
            User user = (User) userService.loadUserByUsername(username);
            GroupEntity group =
                    user.getOwnGroups()
                    .stream()
                    .filter(g -> g.getId().equals(idGroup))
                    .findAny()
                    .orElseGet(()-> user.getGroups()
                            .stream()
                            .filter(g ->g.getId().equals(idGroup))
                            .findAny()
                            .orElseThrow());

            Supplier<Role> checkRoleUser = ()-> group.getOwners().contains(user) ? Role.ROLE_ADMIN : Role.ROLE_USER;
            Role role = checkRoleUser.get();

            List<User> owners = group.getOwners();
            List<User> usersWithoutOwners = group.getUsers().stream().filter(u->!owners.contains(u)).toList();
            Page<UserDto> pages =  userService.searchByLogin("", idGroup, PageRequest.of(page,5));
            List<UserDto> users = pages.getContent();



            model.addAttribute("mode", role)
                    .addAttribute("groupId", idGroup)
                    .addAttribute("group", group)
                    .addAttribute("groupName", group.getName())
                    .addAttribute("users", users)
                    .addAttribute("currentPage", pages.getNumber())
                    .addAttribute("totalPages", pages.getTotalPages())
                    .addAttribute("username", user.getEmail())
                    .addAttribute("currentUserId", user.getId())
                    .addAttribute("usersInGroupWithoutOwners", usersWithoutOwners)
                    .addAttribute("usersInGroup", group.getUsers())
                    .addAttribute("autoOpenTaskId", autoOpenTask ? autoOpenTaskId : null);
            return "calendar";

        } else {
            throw new NoAuthenticationUser();
        }
    }

}
