package com.example.taskmanager.repositories;

import com.example.taskmanager.models.GroupEntity;
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
    public Optional<User> findUserFetchOwnGroupById(Long id) {

        var user = entityManager.createQuery("select u from User u left join fetch u.ownGroups where u.id=?1", User.class)
                .setParameter(1,id)
                .getSingleResult();

        return Optional.of(user);
    }

    @Override
    public void deleteGroupById(Long id) {
        var group = entityManager.createQuery("select g from GroupEntity g where g.id=?1", GroupEntity.class)
                .setParameter(1,id)
                .getSingleResult();

        group.getTasks().clear();
        group.getUsers().clear();

        entityManager.createQuery("delete from GroupEntity g where g.id=?1")
                .setParameter(1,id)
                .executeUpdate();
    }

}
