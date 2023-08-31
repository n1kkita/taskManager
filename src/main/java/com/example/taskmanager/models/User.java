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
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Transient
    private Role role;
    @ManyToMany(mappedBy = "users")
    private List<GroupEntity> groups = new ArrayList<>();
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private GroupEntity ownGroup;
    public Optional<GroupEntity> getOwnGroup() {
        return Optional.ofNullable(ownGroup);
    }
}
