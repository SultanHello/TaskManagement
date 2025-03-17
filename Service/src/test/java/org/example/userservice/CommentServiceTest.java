package org.example.userservice;

import org.example.userservice.dto.CommentDTO;
import org.example.userservice.factory.CommentFactory;
import org.example.userservice.model.Comment;
import org.example.userservice.model.Task;
import org.example.userservice.model.User;
import org.example.userservice.repository.CommentRepository;
import org.example.userservice.repository.TaskRepository;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.service.CommentService;
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