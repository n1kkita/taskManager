package com.example.taskmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @PrePersist
    public void init(){
        dateOfCreate = new Date();
    }
}
