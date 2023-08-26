package com.example.taskmanager.repositories;

import com.example.taskmanager.models.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<User> findUserFetchGroupsAndTaskById(Long id) {

        var user = entityManager.createQuery("select u from User u left join fetch u.ownGroup g where u.id=?1", User.class)
                .setParameter(1,id)
                .getSingleResult();

        return Optional.of(user);
    }

}
