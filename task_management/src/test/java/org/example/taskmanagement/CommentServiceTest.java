package org.example.taskmanagement;

import org.example.taskmanagement.dto.CommentDTO;
import org.example.taskmanagement.factory.CommentFactory;
import org.example.taskmanagement.model.Comment;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.model.User;
import org.example.taskmanagement.repository.CommentRepository;
import org.example.taskmanagement.repository.TaskRepository;
import org.example.taskmanagement.repository.UserRepository;
import org.example.taskmanagement.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentFactory commentFactory;

    @InjectMocks
    private CommentService commentService;

    @Test
    void addComment_success() {
        Task task = new Task();
        User user = User.builder().email("test@mail.com").build();
        CommentDTO commentDto =CommentDTO.builder()
                .text("Test comment")
                .build();
        Comment comment = new Comment();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(commentFactory.createComment(user, task, commentDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment result = commentService.addComment(1L, "test@mail.com", commentDto);

        assertEquals(comment, result);
        verify(commentRepository).save(comment);
    }

    @Test
    void getCommentsByTask_success() {
        Task task = new Task();
        List<Comment> comments = List.of(new Comment());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findCommentsByTask(task)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByTask(1L);

        assertEquals(comments, result);
    }
}