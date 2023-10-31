package com.example.taskmanager.repositories;

import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.GroupHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupHistoryRepository extends JpaRepository<GroupHistory,Long> {
    List<GroupHistory> findByGroup(GroupEntity group);
}
