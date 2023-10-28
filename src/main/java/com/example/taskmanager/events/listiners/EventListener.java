package com.example.taskmanager.events.listiners;

import com.example.taskmanager.events.CreateTaskEvent;
import com.example.taskmanager.models.Task;
import com.example.taskmanager.services.impl.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class EventListener {
    private final EmailSenderService emailSenderService;
    private final TemplateEngine templateEngine;
    @org.springframework.context.event.EventListener
    public void event(CreateTaskEvent event){
        Context context = new Context();
        Task task = (Task) event.getSource();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        context.setVariable("user", task.getUser());
        context.setVariable("dateStart",simpleDateFormat.format(task.getDateOfStart()));
        context.setVariable("dateEnd",simpleDateFormat.format(task.getDateOfEnd()));
        context.setVariable("task",task);
        context.setVariable("currentTime",simpleDateFormat.format(new Date()));

        String htmlContent = templateEngine.process("notificationOfCreateTask", context);
        emailSenderService.send(htmlContent,task.getUser().getEmail(),"Your task save :)");
    }

}
