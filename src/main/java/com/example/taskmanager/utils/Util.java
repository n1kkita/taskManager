package com.example.taskmanager.utils;

import com.example.taskmanager.dto.RegistrationForm;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.exception.EmptyFieldException;
import com.example.taskmanager.exception.InvalidDateException;
import com.example.taskmanager.models.Status;

import java.util.Date;

public class Util {
    public static String replaceToUserLinkInHttpSession(Long id){
        return "successfulUser"+id+"FromForm";
    }

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
        } else if(registrationForm.getName().length()<5 || registrationForm.getPassword().length()<5 || registrationForm.getEmail().length()<5){
            throw new EmptyFieldException("Мінімальна кількість знаків для email і паролю 4 символи");
        }
    }
    public static Status checkStatus(Status status,Date dateOfStart,Date dateOfEnd){
        var currentDate = new Date();

        if(status.equals(Status.CREATED) || !status.equals(Status.DONE) ){
            //Если текущая дата подошла к началу задания меняем статус IN_PROCESS
            if(currentDate.after(dateOfStart)) {
                status = Status.IN_PROCESS;
            }
            //Если дата окончания задания прошла до текущей даты меняем статус на NOT_DONE
            if(dateOfEnd.before(currentDate)) {
                status =  Status.NOT_DONE;
            }
        }
        return status;
    }
}
