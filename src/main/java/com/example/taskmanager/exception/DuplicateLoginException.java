package com.example.taskmanager.exception;

public class DuplicateLoginException extends RuntimeException{
    public DuplicateLoginException(String message) {
        super(message);
    }
}
