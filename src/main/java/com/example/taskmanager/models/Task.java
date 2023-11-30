package com.example.taskmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    @Column(nullable = false)
    private String creatorEmail;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "task",fetch = FetchType.LAZY)
    private List<FileEntity> files = new ArrayList<>();
    public void setFile(FileEntity file){
        file.setTask(this);
        files.add(file);
    }
    @PrePersist
    public void init(){
        dateOfCreate = new Date();
    }
}
