package com.example.taskmanager.repositories;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository< Task, Long > {

    @Query("select new com.example.taskmanager.dto.TaskDto(t.id,t.title,t.description,t.status,t.dateOfEnd,t.dateOfStart,t.group.id,t.user.id,t.user.email,-1L) " +
            "from Task t where t.group.id = ?1")
    List<TaskDto> findAllByGroupId(Long id);

    @Query("select new com.example.taskmanager.dto.TaskDto(t.id,t.title,t.description,t.status,t.dateOfEnd,t.dateOfStart,t.group.id,t.user.id,t.user.email,-1L) from Task t")
    Page<TaskDto> findAllTasks(Pageable pageable);

}
