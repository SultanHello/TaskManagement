package org.example.taskmanagement;

import org.example.taskmanagement.dto.TaskCreator;
import org.example.taskmanagement.model.*;
import org.example.taskmanagement.repository.TaskRepository;
import org.example.taskmanagement.repository.UserRepository;
import org.example.taskmanagement.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_success() {
        User author = User.builder().email("test@mail.com").build();
        TaskCreator taskCreator = new TaskCreator("Title", "Desc", null, null);
        Task task = Task.builder().author(author).title("Title").description("Desc").build();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(taskCreator, author);

        assertEquals(task, result);
    }

    @Test
    void takeTask_success() {
        User user = User.builder().email("test@mail.com").build();
        Task task = Task.builder().id(1L).build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.takeTask(1L, "test@mail.com");

        assertEquals(user, result.getAssignee());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void isAssigneeOrAdmin_admin_returnsTrue() {
        User user = User.builder().email("admin@mail.com").role(Role.ADMIN).build();
        Task task = Task.builder().id(1L).build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("admin@mail.com")).thenReturn(Optional.of(user));

        boolean result = taskService.isAssigneeOrAdmin(1L, "admin@mail.com");

        assertTrue(result);
    }
}