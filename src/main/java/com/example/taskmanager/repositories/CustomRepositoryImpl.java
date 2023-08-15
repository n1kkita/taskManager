package com.example.taskmanager.repositories;

import com.example.taskmanager.models.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

    private final EntityManager entityManager;

    @Override
    public User findAllFetchGroupsAndTaskAndOwnerGroup() {
        return null;
    }
}
