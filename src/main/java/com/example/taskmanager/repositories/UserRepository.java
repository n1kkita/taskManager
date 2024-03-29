package com.example.taskmanager.repositories;


import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository< User,Long >, CustomRepository {
    @Override
    @Query("select u from User u left join fetch u.groups")
    List< User > findAll();

    @Query("select new com.example.taskmanager.dto.UserDto(u.id,u.email,u.name) from User u")
    Page< UserDto> findAllUserDto(Pageable pageable);

    @Query("select u from User u left join fetch u.ownGroups where u.id=?1")
    User findUserByIdFetchOwnGroups(Long id);

    @Query("SELECT new com.example.taskmanager.dto.UserDto(u.id, u.email,u.name) FROM User u WHERE u.email LIKE %?1% AND NOT EXISTS (SELECT 1 FROM u.groups g WHERE g.id = ?2)")
    Page< UserDto > searchAllByLogin(String login,Long id,Pageable pageable);

    @Query("select u.email from User u where u.email=?1")
    Optional<String> getLoginByCreateLogin(String login);
    @Query("select new com.example.taskmanager.dto.UserDto(u.id, u.email,u.name) from User u where u.id=?1")
    Optional<UserDto> findUserById(Long id);
    Optional<User> findByEmail(String email);
}
