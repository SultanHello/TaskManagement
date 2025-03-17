package org.example.taskmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskmanagement.dto.CommentDTO;
import org.example.taskmanagement.model.Comment;
import org.example.taskmanagement.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Work with comments for tasks")
public class CommentController {
    private final CommentService commentService;


    @PostMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isAssigneeOrAdmin(#taskId, authentication.name)")
    @Operation(summary = "Add a comment", description = "Add a new comment to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added"),
            @ApiResponse(responseCode = "400", description = "Bad request, wrong data")
    })
    public ResponseEntity<Comment> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentDTO commentDto,
            Authentication authentication) {
        try {
            Comment comment = commentService.addComment(taskId, authentication.getName(), commentDto);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or @taskService.isAssigneeOrAdmin(#taskId, authentication.name)")
    @Operation(summary = "Get comments", description = "Get all comments for a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments found"),
            @ApiResponse(responseCode = "400", description = "Bad request, no comments")
    })
    public ResponseEntity<List<Comment>> getCommentsByTask(
            @PathVariable Long taskId) {
        try {
            return ResponseEntity.ok(commentService.getCommentsByTask(taskId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }
}