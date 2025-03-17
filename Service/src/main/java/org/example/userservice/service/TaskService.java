package org.example.userservice.service;


import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.example.userservice.dto.TaskCreator;
import org.example.userservice.exeption.TaskNotFoundException;
import org.example.userservice.exeption.UserNotFoundException;
import org.example.userservice.model.*;
import org.example.userservice.repository.TaskRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public Task createTask(TaskCreator taskCreator, User author) {
        Task task = Task.builder()
                .author(author)
                .title(taskCreator.getTitle())
                .description(taskCreator.getDescription())
                .build();
        return taskRepository.save(task);

    }

    public List<Task> getTasksByAuthor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));

        return taskRepository.findTasksByAuthor(user);

    }
    @Transactional
    public Task takeTask(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));

        if (task.getAssignee() != null) {
            throw new IllegalStateException("Задача уже назначена пользователю " + task.getAssignee().getEmail());
        }



        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        task.setAssignee(user);
        task.setStatus(Status.IN_PROGRESS);
        return taskRepository.save(task);
    }
    @Transactional
    public void completeTask(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));

        if (task.getAssignee() == null || !task.getAssignee().getEmail().equals(username)) {
            throw new IllegalStateException("You are not assigned to this task");
        }


        task.setStatus(Status.COMPLETED);
        taskRepository.save(task);

    }
    public boolean isAssigneeOrAdmin(Long taskId, String userEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getRole() == Role.ADMIN ||
                (task.getAssignee() != null && task.getAssignee().getEmail().equals(userEmail));
    }

    public List<Task> getAllTasksFilter(int page, int size, Status status, Priority priority) {
        Pageable pageable =PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Task> taskPage;


        if (status != null && priority != null) {
            taskPage = taskRepository.findByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            taskPage = taskRepository.findByStatus(status, pageable);
        } else if (priority != null) {
            taskPage = taskRepository.findByPriority(priority, pageable);
        } else {
            taskPage = taskRepository.findAll(pageable);
        }

        return taskPage.getContent();
    }

    @Transactional
    public Task editTask(Long taskId, TaskCreator taskCreator) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));


        if (StringUtils.isNotBlank(taskCreator.getTitle())) {
            task.setTitle(taskCreator.getTitle());
        }
        if (StringUtils.isNotBlank(taskCreator.getDescription())) {
            task.setDescription(taskCreator.getDescription());
        }
        if (taskCreator.getStatus() != null) {
            task.setStatus(taskCreator.getStatus());
        }
        if (taskCreator.getPriority() != null) {
            task.setPriority(taskCreator.getPriority());
        }

        return taskRepository.save(task);
    }
    @Transactional
    public Task assignTask(Long taskId, String assigneeEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));

        User assignee = userRepository.findByEmail(assigneeEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + assigneeEmail + " not found"));
        if (task.getAssignee() != null) {
            throw new IllegalStateException("Task already assigned to " + task.getAssignee().getEmail());
        }
        task.setAssignee(assignee);
        task.setStatus(Status.IN_PROGRESS);
        return taskRepository.save(task);
    }
    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));
        taskRepository.delete(task);

    }
    public boolean isAuthor(Long taskId, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return task.getAuthor().getEmail().equals(email);
    }




}
