package com.example.taskmanager.exception;

public class NoAuthenticationUser extends RuntimeException {
    public NoAuthenticationUser() {
        super("User is not authorized");
    }
}
