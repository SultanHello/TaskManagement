package org.example.taskmanagement.repository;

import org.example.taskmanagement.model.Priority;
import org.example.taskmanagement.model.Status;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findTasksByAuthor(User author);

    Page<Task> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);
}
