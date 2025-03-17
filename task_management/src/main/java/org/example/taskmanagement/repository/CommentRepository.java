package org.example.taskmanagement.repository;

import org.example.taskmanagement.model.Comment;
import org.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByTask(Task task);

}
