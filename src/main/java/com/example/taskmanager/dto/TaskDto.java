package com.example.taskmanager.dto;

import com.example.taskmanager.models.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDto  {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Date dateOfEnd;
    private Date dateOfStart;
    private Long groupId;
    private Long userId;
    private String appointedUserEmail;
    private String creatorEmail;

}
