package com.example.taskmanager.utils;

import com.example.taskmanager.models.Status;
import com.example.taskmanager.services.interfaceses.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class Util {
    private final UserService service;

    public static String replaceToUserLinkInHttpSession(Long id){
        return "successfulUser"+id+"FromForm";
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
