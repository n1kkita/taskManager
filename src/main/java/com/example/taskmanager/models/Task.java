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
    private Date dateOfCreate;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date dateOfStart;
    @Column(nullable = false)
    private Date dateOfEnd;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean allDay;

    @ManyToOne
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "user_task_fk"))
    private User user;
    @PrePersist
    public void init(){
        dateOfCreate = new Date();
        status = Status.CREATED;
    }
}
