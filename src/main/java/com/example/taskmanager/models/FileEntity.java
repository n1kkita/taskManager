package com.example.taskmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contentType;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "task_id",foreignKey = @ForeignKey(name = "task_file_fk"))
    private Task task;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

}
