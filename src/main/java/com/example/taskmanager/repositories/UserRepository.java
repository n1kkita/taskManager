package com.example.taskmanager.repositories;


import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository< User,Long >, CustomRepository {
    @Override
    @Query("select u from User u left join fetch u.groups")
    List< User > findAll();
    @Query("select u.id from User u where u.email=?1 and u.password=?2")
    Optional<Long> findUserIdByLoginAndPassword(String login, String password);
    @Query("select new com.example.taskmanager.dto.UserDto(u.id,u.email) from User u")
    Page< UserDto> findAllUserDto(Pageable pageable);

    @Query("SELECT new com.example.taskmanager.dto.UserDto(u.id, u.email) FROM User u WHERE u.email LIKE %?1% AND NOT EXISTS (SELECT 1 FROM u.groups g WHERE g.id = ?2)")
    Page< UserDto > searchAllByLogin(String login,Long id,Pageable pageable);

    @Query("select u.email from User u where  u.id=?1")
    Optional<String> findLoginById(Long id);
    @Query("select u.email from User u where u.email=?1")
    Optional<String> getLoginByCreateLogin(String login);

    Optional<User> findByEmail(String email);
}
