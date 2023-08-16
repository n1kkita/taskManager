package com.example.taskmanager.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.*;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Override
    public String toString() {
        return "User{}";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @ManyToMany(mappedBy = "users")
    private List<GroupEntity> groups = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GroupEntity ownGroup;
    public void addTask(Task task){
        tasks.add(task);
        task.setUser(this);
    }
}
