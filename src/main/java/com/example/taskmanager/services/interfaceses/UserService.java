package com.example.taskmanager.services.interfaceses;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    Page< UserDto > getAll(Pageable pageable);
    Page< UserDto > searchByLogin(String login,Long idGroup,Pageable pageable);
    User create(RegistrationForm form);
    User getUserById(Long id);
    UserDto getDtoById(Long id);
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
