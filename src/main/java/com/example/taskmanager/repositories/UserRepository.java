package com.example.taskmanager.repositories;


import com.example.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository< User,Long >, CustomRepository {
    @Override
    @Query("select u from User u left join fetch u.groups")
    List< User > findAll();

    User findByLogin(String login);
    Optional< User> findByLoginAndPassword(String login,String password);
}
