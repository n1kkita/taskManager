package com.example.taskmanager.utils;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.exception.EmptyFieldException;
import com.example.taskmanager.exception.InvalidDateException;
import com.example.taskmanager.models.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Component
@RequiredArgsConstructor
public class Util {
    public static void validation(TaskDto taskDto){
        //Проверка на корректность данных
        if(taskDto.getDateOfEnd().before(taskDto.getDateOfStart()) ||
                taskDto.getDateOfEnd().equals(taskDto.getDateOfStart()))
            throw new InvalidDateException("Введені не корректні данні." +
                    " Перевірте чи заповнили ви всі поля і чи правильно ви вказали дату");

        if(taskDto.getUserId() == null || taskDto.getTitle().isEmpty() || taskDto.getDescription().isEmpty())
            throw new EmptyFieldException("Помилка збереження завдання. Перевірьте чи заповниили ви всі поля");
    }
    public static void validation(RegistrationForm registrationForm){
        if(registrationForm.getEmail().isEmpty() || registrationForm.getPassword().isEmpty() || registrationForm.getName().isEmpty()){
            throw new EmptyFieldException("Поля не мають бути пустими. Мінімальна кількість знаків для логіну і паролю 4 символи");
        } else if(registrationForm.getName().length() < 3 || registrationForm.getPassword().length() < 6 || registrationForm.getEmail().length() < 6){
            throw new EmptyFieldException("Мінімальна кількість знаків для імені: 3, для паролю: 6");
        }
    }
    @Transactional
    public Status checkStatus(Status status,Date dateOfStart,Date dateOfEnd){
        var currentDate = new Date();
        if(status.equals(Status.CREATED) || !status.equals(Status.DONE) ){

            //Если дата окончания задания прошла до текущей даты меняем статус на NOT_DONE
            if(dateOfEnd.before(currentDate)) {
                return Status.NOT_DONE;
            }
            //Если текущая дата подошла к началу задания меняем статус IN_PROCESS
            if(currentDate.after(dateOfStart)) {
                return Status.IN_PROCESS;
            }

        }
        return status;
    }
}
