package com.example.taskmanager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private LocalDateTime dateOfCreate;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date dateOfEnd;
    @Column(nullable = false,unique = true)
    @Enumerated(EnumType.STRING)
    private Status status;
    @PrePersist
    public void init(){
        dateOfCreate = LocalDateTime.now();
    }
}
