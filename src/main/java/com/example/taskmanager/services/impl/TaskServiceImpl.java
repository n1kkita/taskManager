package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.models.GroupEntity;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.TaskRepository;
import com.example.taskmanager.services.interfaceses.GroupService;
import com.example.taskmanager.services.interfaceses.TaskService;
import com.example.taskmanager.services.interfaceses.UserService;
import com.example.taskmanager.utils.Util;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final GroupService groupService;
    private final UserService userService;
    @Override
    public List< TaskDto > getAllByGroupId(Long groupId) {
        return taskRepository.findAllByGroupId(groupId).stream()
                .peek(task -> task.
                        setStatus(Util.checkStatus(task.getStatus(),task.getDateOfStart(),task.getDateOfEnd()))
                ).toList();
    }
    @Override
    public TaskDto saveTask(TaskDto taskDto) {

        GroupEntity group = groupService.getById(taskDto.getGroupId());
        User user = userService.getUserById(taskDto.getUserId());

        Task task = new Task();

        task.setGroup(group);
        task.setDescription(taskDto.getDescription());
        task.setTitle(taskDto.getTitle());
        task.setDateOfStart(taskDto.getDateOfStart());
        task.setDateOfEnd(taskDto.getDateOfEnd());
        task.setUser(user);
        task.setStatus(Util.checkStatus(Status.CREATED,task.getDateOfStart(),task.getDateOfEnd()));
        taskRepository.save(task);

        taskDto.setId(task.getId());
        taskDto.setStatus(task.getStatus());

        return taskDto;
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task updateTaskById(Long id, TaskDto editTask) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isPresent()) {
            var currentDay = new Date();
            Task task = taskOptional.get();

            task.setTitle(editTask.getTitle());
            task.setDescription(editTask.getDescription());
            task.setDateOfStart(editTask.getDateOfStart());
            task.setDateOfEnd(editTask.getDateOfEnd());
            task.setDateOfCreate(new Date()); //Перезаписываем дату создания
            task.setUser(userService.getUserById(editTask.getUserId()));

            if(currentDay.before(task.getDateOfStart())){
                task.setStatus(Status.CREATED);

            } else if(currentDay.after(task.getDateOfStart())){
                task.setStatus(Status.IN_PROCESS);

            } else if(task.getDateOfEnd().before(currentDay)){
                task.setStatus(Status.NOT_DONE);
            }
        }

        return taskOptional.orElseThrow(() -> new EntityNotFoundException("Ошибка при обновлении задачи"));
    }

    @Override
    public void updateTaskStatusById(Long id) {
        taskRepository.findById(id).ifPresent(task -> task.setStatus(Status.DONE));
    }
}
