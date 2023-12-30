package com.example.taskmanager.controllers.api;

import com.example.taskmanager.dto.UserDto;
import com.example.taskmanager.events.RemoveCodeEvent;
import com.example.taskmanager.events.VerificationEvent;
import com.example.taskmanager.events.publisher.EventPublisher;
import com.example.taskmanager.exception.CodeExpiredException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/verification/{userEmail}")
@RequiredArgsConstructor
public class VerificationController {
    private final EventPublisher eventPublisher;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0); // Создает новый поток для каждой задачи

    @PostMapping
    public void generateRandomCode(@PathVariable String userEmail, HttpSession session){
        Integer randomCode = 1000 + new Random().nextInt(9000);
        session.setAttribute("randomCode_" + userEmail, randomCode);

        System.out.println("user_email:" + userEmail);

        eventPublisher.publish(new VerificationEvent(new UserDto(-1L,userEmail,"null"), randomCode));
        // Планируем выполнение события через 3 минут
        scheduler.schedule(() -> {
            // После 3 минут опубликуем событие
            eventPublisher.publish(new RemoveCodeEvent("randomCode_" + userEmail,session));
        }, 3, TimeUnit.MINUTES);
    }

    @GetMapping
    public ResponseEntity<Boolean> verification(@PathVariable String userEmail, @RequestParam Integer userCode, HttpSession session){
        Integer correctCode = (Integer) session.getAttribute("randomCode_" + userEmail);
        if(correctCode == null){
            throw new CodeExpiredException("Термін дії коду минув");
        }

        boolean isCorrect = correctCode.intValue() == userCode.intValue();
        if(isCorrect){
            session.removeAttribute("randomCode_" + userEmail);
        }
        HttpStatus status = isCorrect ? HttpStatus.OK : HttpStatus.CONFLICT;
        return ResponseEntity
                .status(status)
                .body(isCorrect);
    }
}
