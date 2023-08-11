package com.example.taskmanager.dto;

import com.example.taskmanager.models.Status;

import java.util.Date;

public record TaskDto(String title, String description, Status status, Date dateOfEnd) {
}
