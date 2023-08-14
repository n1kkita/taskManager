package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.interfaceses.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    @Override
    public List< TaskDto > getAll() {
        var currentDate = new Date();
        return taskRepository.findAll().stream().map(task -> {

            if(task.getStatus().equals(Status.CREATED) || !task.getStatus().equals(Status.DONE) ){
                //Если текущая дата подошла к началу задания меняем статус IN_PROCESS
                if(currentDate.after(task.getDateOfStart())) {
                    System.out.println("Статус изменен на IN_PROCESS");
                    task.setStatus(Status.IN_PROCESS);
                }
                //Если дата окончания задания прошла до текущей даты меняем статус на NOT_DONE
                if(task.getDateOfEnd().before(currentDate)) {
                    System.out.println("Статус изменен на NOT_DONE");
                    task.setStatus(Status.NOT_DONE);
                }

            }
            return new TaskDto(task.getId(),task.getTitle(),task.getDescription(),
                    task.getStatus(),task.getDateOfEnd(),task.getDateOfStart(), task.isAllDay());
        }).toList();
    }
    @Override
    public TaskDto getById(Long id) {
        Optional<TaskDto> taskDto = taskRepository.findTaskById(id);
        return taskDto.orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void updateTaskById(Long id, Task editTask) {
        taskRepository.findById(id)
                .ifPresent(task -> {
                    task.setTitle(editTask.getTitle());
                    task.setDescription(editTask.getDescription());
                    task.setDateOfStart(editTask.getDateOfStart());
                    task.setDateOfEnd(editTask.getDateOfEnd());
                    task.setDateOfCreate(new Date());
                    if(editTask.getStatus() != null){
                        task.setStatus(editTask.getStatus());
                    }
                });
    }
}
