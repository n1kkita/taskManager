package com.example.taskmanager.services.impl.user;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.exception.DuplicateLoginException;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.UserRepository;
import com.example.taskmanager.services.interfaceses.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.taskmanager.utils.Util.validation;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Page< UserDto > getAll(Pageable pageable) {
        return userRepository.findAllUserDto(pageable);
    }

    @Override
    public Page< UserDto > searchByLogin(String login,Long idGroup,Pageable pageable) {
        return userRepository.searchAllByLogin(login,idGroup,pageable);
    }

    @Override
    @Transactional
    public User create(RegistrationForm form) {
        User user = new User();

        user.setEmail(form.getEmail());
        user.setName(form.getName());
        user.setPassword(passwordEncoder.encode(form.getPassword()));

        return userRepository.save(user);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findUserFetchOwnGroupById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found"));
    }
    @Override
    public UserDto getDtoById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow();
    }


}
