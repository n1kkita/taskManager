package com.example.taskmanager.exception;

public class InvalidDateException extends RuntimeException{

    public InvalidDateException(String message) {
        super(message);
    }
}
