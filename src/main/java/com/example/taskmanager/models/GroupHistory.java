package com.example.taskmanager.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "group_histories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;
    @ManyToOne
    private GroupEntity group;
    @Column(nullable = false)
    private LocalDateTime saveAt;

    @PrePersist
    public void init(){
        saveAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

}
