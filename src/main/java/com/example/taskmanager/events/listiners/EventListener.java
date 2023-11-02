package com.example.taskmanager.events.listiners;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.events.PerformingTaskWithSendingFile;
import com.example.taskmanager.events.UpdateTaskStatusEvent;
import com.example.taskmanager.models.Status;
import com.example.taskmanager.services.impl.email.EmailSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
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
        Context context = new Context();
        TaskDto task = (TaskDto) event.getSource();

        // Установить часовой пояс Киева (UTC+2 или UTC+3)
        TimeZone timeZoneKiev = TimeZone.getTimeZone("Europe/Kiev");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        simpleDateFormat.setTimeZone(timeZoneKiev);

        context.setVariable("user_email", task.getEmail());
        context.setVariable("dateStart", simpleDateFormat.format(task.getDateOfStart()));
        context.setVariable("dateEnd", simpleDateFormat.format(task.getDateOfEnd()));
        context.setVariable("task", task);
        context.setVariable("currentTime", simpleDateFormat.format(new Date()));
        context.setVariable("statusColor", getColorByStatus(task.getStatus()));

        String htmlContent = templateEngine.process("notificationOfCreateTask", context);
        emailSenderService.send(htmlContent, task.getEmail(), "Task status update :)");
    }
    @org.springframework.context.event.EventListener
    public void event(PerformingTaskWithSendingFile event) {
        emailSenderService.sendWithFile("Task was completed",event.getFile());
    }

    public String getColorByStatus(Status status) {
        return switch (status) {
            case CREATED -> "#6fb6fa"; // Синий цвет для CREATED
            case IN_PROCESS -> "#f67452"; // Оранжевый цвет для IN_PROCESS
            case DONE -> "#5cef5c"; // Зеленый цвет для DONE
            case NOT_DONE -> "#f84d4d"; // Красный цвет для NOT_DONE
        };
    }

}
