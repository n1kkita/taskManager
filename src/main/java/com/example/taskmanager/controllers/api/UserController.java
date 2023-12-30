package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.exception.DuplicateLoginException;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.interfaceses.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    @GetMapping
    public Page< UserDto > getAll(Pageable pageable){
        return userService.getAll(pageable);
    }
    @GetMapping("/search")
    public Page< UserDto > getAll(@RequestParam String login,@RequestParam Long idGroup){
        return userService.searchByLogin(login, idGroup, PageRequest.of(0,5));
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userRepository.findUserByIdFetchOwnGroups(id);
    }
    @GetMapping("/{id}/dto")
    public UserDto getDtoById(@PathVariable Long id){
        return userService.getDtoById(id);
    }
    @GetMapping("/check_email/{email}")
    public Boolean checkOnDuplicated(@PathVariable String email){
        userRepository.getLoginByCreateLogin(email).ifPresent(string -> {
            throw new DuplicateLoginException("Користувач с таким логіном уже є, виберіть інший");
        });

        return false;
    }

}
