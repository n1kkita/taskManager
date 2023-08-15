package com.example.taskmanager.repositories;


import com.example.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface UserRepository extends JpaRepository< User,Long >, CustomRepository {
    @Override
    @Query("select u from User u left join fetch u.groups")
    List< User > findAll();
}
