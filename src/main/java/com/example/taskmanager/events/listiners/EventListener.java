package com.example.taskmanager.events.listiners;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.events.*;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.models.Type;
import com.example.taskmanager.models.User;
import com.example.taskmanager.services.impl.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class EventListener {
    private final TimeZone timeZoneKiev = TimeZone.getTimeZone("Europe/Kiev");;
    private final Context context = new Context();
    private final EmailSenderService emailSenderService;
    private final TemplateEngine templateEngine;
    private final SimpleDateFormat simpleDateFormat;
    @Autowired
    public EventListener(EmailSenderService emailSenderService,TemplateEngine templateEngine){
        this.emailSenderService = emailSenderService;
        this.templateEngine = templateEngine;
        this.simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        context.setVariable("currentTime", simpleDateFormat.format(new Date()));
        simpleDateFormat.setTimeZone(timeZoneKiev);
    }
    @org.springframework.context.event.EventListener
    public void event(UpdateTaskStatusEvent event) {
        TaskDto taskDto = (TaskDto) event.getSource();
        String html = htmlWithoutFiles(taskDto,"notificationOfUpdateStatusTask");
        emailSenderService.send(html, taskDto.getAppointedUserEmail(), "Task status update");
    }
    @org.springframework.context.event.EventListener
    public void event(PerformingTaskWithSendingFile event) {
        Task task = (Task) event.getSource();
        String html = htmlWithFiles(task,"notificationOfCompletedTask");
        emailSenderService.send(html,task.getCreatorEmail(),"The user has completed your task");
    }
    @org.springframework.context.event.EventListener
    public void event(CreateTaskEvent event) {
        Task task = (Task) event.getSource();
        String html = htmlWithFiles(task,"notificationOfCreateTask");
        emailSenderService.send(html,task.getUser().getEmail(),"You have been assigned a new task");
    }
    @org.springframework.context.event.EventListener
    public void event(VerificationEvent event) {
        UserDto userDto = (UserDto) event.getSource();
        Integer verificationCode = event.getVerificationCode();
        String html = htmlVerification(userDto,verificationCode,"verificationEmail");
        emailSenderService.send(html,userDto.getEmail(),"Task Manager. Email confirmation");
    }

    @org.springframework.context.event.EventListener
    public void event(RemoveCodeEvent event) {
        String link = (String) event.getSource();
        System.out.println("Link was deleted:" + link);
        event.getSession().removeAttribute(link);
    }

    public String getColorByStatus(Status status) {
        return switch (status) {
            case CREATED -> "#6fb6fa"; // Синий цвет для CREATED
            case IN_PROCESS -> "#f67452"; // Оранжевый цвет для IN_PROCESS
            case DONE -> "#5cef5c"; // Зеленый цвет для DONE
            case NOT_DONE -> "#f84d4d"; // Красный цвет для NOT_DONE
        };
    }

    public String htmlWithFiles(Task task,String template){

        context.setVariable("user_email", task.getUser().getEmail());
        context.setVariable("dateStart", simpleDateFormat.format(task.getDateOfStart()));
        context.setVariable("dateEnd", simpleDateFormat.format(task.getDateOfEnd()));
        context.setVariable("task", task);
        context.setVariable("statusColor", getColorByStatus(task.getStatus()));
        context.setVariable("owner_email",task.getCreatorEmail());
        context.setVariable("files_owner",task.getFiles().stream().filter(fileEntity -> fileEntity.getType().equals(Type.OWNER_FILE)).toList());
        context.setVariable("files_user",task.getFiles().stream().filter(fileEntity -> fileEntity.getType().equals(Type.USER_FILE)).toList());

        return templateEngine.process(template, context);
    }

    public String htmlWithoutFiles(TaskDto taskDto,String template){

        context.setVariable("user_email", taskDto.getAppointedUserEmail());
        context.setVariable("currentTime", simpleDateFormat.format(new Date()));
        context.setVariable("dateStart", simpleDateFormat.format(taskDto.getDateOfStart()));
        context.setVariable("dateEnd", simpleDateFormat.format(taskDto.getDateOfEnd()));
        context.setVariable("taskDto", taskDto);
        context.setVariable("statusColor", getColorByStatus(taskDto.getStatus()));
        context.setVariable("owner_email",taskDto.getCreatorEmail());

        return templateEngine.process(template, context);
    }
    public String htmlVerification(UserDto userDto,Integer verificationCode,String template){

        context.setVariable("userDto", userDto);
        context.setVariable("verificationCode",verificationCode);

        return templateEngine.process(template, context);
    }


}
