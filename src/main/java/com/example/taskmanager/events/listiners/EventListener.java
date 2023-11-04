package com.example.taskmanager.events.listiners;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.events.PerformingTaskWithSendingFile;
import com.example.taskmanager.events.UpdateTaskStatusEvent;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.services.impl.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final EmailSenderService emailSenderService;
    private final TemplateEngine templateEngine;

    @org.springframework.context.event.EventListener
    public void event(UpdateTaskStatusEvent event) {
        TaskDto taskDto = (TaskDto) event.getSource();
        String html = html((TaskDto) event.getSource(),"notificationOfUpdateStatusTask",false);
        emailSenderService.send(html, taskDto.getAppointedUserEmail(), "Task status update");
    }
    @org.springframework.context.event.EventListener
    public void event(PerformingTaskWithSendingFile event) {
        TaskDto taskDto = (TaskDto) event.getSource();
        String html = html((TaskDto) event.getSource(),"notificationOfCompletedTask",true);
        emailSenderService.sendWithFile(html,"The user has completed your task",taskDto.getCreatorEmail(),event.getFiles());
    }

    public String getColorByStatus(Status status) {
        return switch (status) {
            case CREATED -> "#6fb6fa"; // Синий цвет для CREATED
            case IN_PROCESS -> "#f67452"; // Оранжевый цвет для IN_PROCESS
            case DONE -> "#5cef5c"; // Зеленый цвет для DONE
            case NOT_DONE -> "#f84d4d"; // Красный цвет для NOT_DONE
        };
    }
    public String html(TaskDto task,String template,boolean isWithFiles){
        Context context = new Context();
        // Установить часовой пояс Киева (UTC+2 или UTC+3)
        TimeZone timeZoneKiev = TimeZone.getTimeZone("Europe/Kiev");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        simpleDateFormat.setTimeZone(timeZoneKiev);

        context.setVariable("user_email", task.getAppointedUserEmail());
        context.setVariable("currentTime", simpleDateFormat.format(new Date()));
        context.setVariable("dateStart", simpleDateFormat.format(task.getDateOfStart()));
        context.setVariable("dateEnd", simpleDateFormat.format(task.getDateOfEnd()));
        context.setVariable("task", task);
        context.setVariable("statusColor", getColorByStatus(task.getStatus()));
        if(isWithFiles){
            context.setVariable("owner_email",task.getCreatorEmail());
        }

        return templateEngine.process(template, context);
    }


}
