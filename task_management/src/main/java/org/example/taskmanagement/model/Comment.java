package org.example.taskmanagement.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnore
    private User author;
}