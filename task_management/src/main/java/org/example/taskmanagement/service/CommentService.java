package org.example.taskmanagement.service;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.example.taskmanagement.dto.CommentDTO;
import org.example.taskmanagement.factory.CommentFactory;
import org.example.taskmanagement.model.Comment;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.model.User;
import org.example.taskmanagement.repository.CommentRepository;
import org.example.taskmanagement.repository.TaskRepository;
import org.example.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentFactory commentFactory;
    private final TaskRepository taskRepository;

    public Comment addComment(Long taskId, String email, CommentDTO commentDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        try{
            Comment comment = commentFactory.createComment(user,task,commentDto);
            return commentRepository.save(comment);
        }catch (RuntimeException e){
            throw new RuntimeException("error while work with database");
        }

    }

    public List<Comment> getCommentsByTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return commentRepository.findCommentsByTask(task);
    }
}
