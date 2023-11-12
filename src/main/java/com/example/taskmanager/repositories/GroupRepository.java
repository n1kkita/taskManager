package com.example.taskmanager.repositories;

import com.example.taskmanager.models.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository< GroupEntity,Long > , CustomRepository {
}
