package com.example.taskmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDateException(InvalidDateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<String> handleCodeExpiredException(CodeExpiredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.getMessage());
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<String> handleEmptyFieldException(EmptyFieldException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateLoginException.class)
    public ResponseEntity<String> handleDuplicateLoginException(DuplicateLoginException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NoAuthenticationUser.class)
    public String handleDuplicateNoAuthenticationUser(NoAuthenticationUser ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("errorNoAuthenticationUser",ex.getMessage());

        return "redirect:/authentication";
    }


}
