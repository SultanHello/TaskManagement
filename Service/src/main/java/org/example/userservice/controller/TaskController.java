package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.TaskCreator;
import org.example.userservice.model.Priority;
import org.example.userservice.model.Status;
import org.example.userservice.model.Task;
import org.example.userservice.model.User;
import org.example.userservice.service.TaskService;
import org.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Work with tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @GetMapping("/filter")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get tasks with filter", description = "Find tasks by status and priority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found")
    })
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority) {
        return ResponseEntity.ok(taskService.getAllTasksFilter(page, size, status, priority));
    }

    @GetMapping("/by-author")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get tasks by author", description = "Find tasks by author email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found")
    })
    public ResponseEntity<List<Task>> getTasksByAuthor(@Valid @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksByAuthor(email));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Create a task", description = "Add a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskCreator taskCreator, Authentication authentication) {
        if (authentication == null) {
            System.out.println(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication is null"));
        }
        User user = userService.findByEmail(authentication.getName());
        Task task = taskService.createTask(taskCreator, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PatchMapping("/{taskId}/take")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isAssigneeOrAdmin(#taskId, authentication.name)")
    @Operation(summary = "Take a task", description = "User takes a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task taken")
    })
    public ResponseEntity<Task> takeTask(@PathVariable Long taskId, Authentication authentication) {
        return ResponseEntity.ok(taskService.takeTask(taskId, authentication.getName()));
    }

    @PatchMapping("/{taskId}/complete")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isAssigneeOrAdmin(#taskId, principal.getName())")
    @Operation(summary = "Complete a task", description = "Mark task as completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task completed")
    })
    public ResponseEntity<String> completeTask(@PathVariable Long taskId, Principal principal) {
        taskService.completeTask(taskId, principal.getName());
        return ResponseEntity.ok("Task completed successfully");
    }

    @PutMapping("/{taskId}/edit")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isAssigneeOrAdmin(#taskId, authentication.name)")
    @Operation(summary = "Edit a task", description = "Change task details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated")
    })
    public ResponseEntity<Task> editTask(@PathVariable Long taskId, @RequestBody TaskCreator taskCreator, Authentication authentication) {
        return ResponseEntity.ok(taskService.editTask(taskId, taskCreator));
    }

    @PatchMapping("/{taskId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign a task", description = "Give task to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task assigned")
    })
    public ResponseEntity<Task> assignTask(@PathVariable Long taskId, @RequestParam String assigneeEmail) {
        return ResponseEntity.ok(taskService.assignTask(taskId, assigneeEmail));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a task", description = "Remove a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted")
    })
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }
}