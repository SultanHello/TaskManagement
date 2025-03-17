package org.example.userservice.repository;

import org.example.userservice.model.Comment;
import org.example.userservice.model.Task;
import org.example.userservice.model.User;
import org.hibernate.dialect.function.LpadRpadPadEmulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByTask(Task task);

}
