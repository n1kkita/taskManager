package com.example.taskmanager.services.impl;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.exception.EmptyFieldException;
import com.example.taskmanager.exception.InvalidDateException;
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

        //Проверка на корректность данных
        if(taskDto.getDateOfEnd().before(taskDto.getDateOfStart()) ||
                taskDto.getDateOfEnd().equals(taskDto.getDateOfStart()))
            throw new InvalidDateException("Введена не корректные данные." +
                    " Проверьте заполнили ли вы все поля и правильно ли вы казали дату");

        if(taskDto.getUserId() == null || taskDto.getTitle() == null || taskDto.getDescription() == null)
            throw new EmptyFieldException("Ошибка сохранения задания.Проверьте заполнили ли вы все поля");


        GroupEntity group = groupService.getById(taskDto.getGroupId());
        User user = userService.getUserById(taskDto.getUserId());

        Task task = Task.builder()
                .group(group)
                .description(taskDto.getDescription())
                .title(taskDto.getTitle())
                .user(user)
                .dateOfStart(taskDto.getDateOfStart())
                .dateOfEnd(taskDto.getDateOfEnd())
                .status(Util.checkStatus(Status.CREATED,taskDto.getDateOfStart(),taskDto.getDateOfEnd()))
                .build();
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
    public void updateTaskById(Long id, TaskDto editTask) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        taskOptional.map(task -> {

            task.setTitle(editTask.getTitle());
            task.setDescription(editTask.getDescription());
            task.setDateOfStart(editTask.getDateOfStart());
            task.setDateOfEnd(editTask.getDateOfEnd());
            task.setDateOfCreate(new Date()); //Перезаписываем дату создания
            task.setUser(userService.getUserById(editTask.getUserId()));
            task.setStatus(Util.checkStatus(Status.CREATED,task.getDateOfStart(),task.getDateOfEnd()));
            return task;

        }).orElseThrow(() -> new EntityNotFoundException("Ошибка при обновлении задачи"));

    }

    @Override
    public void updateTaskStatusById(Long id) {
        taskRepository.findById(id).ifPresent(task -> task.setStatus(Status.DONE));
    }
}
