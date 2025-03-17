package org.example.taskmanagement.factory;


import org.example.taskmanagement.dto.CommentDTO;
import org.example.taskmanagement.model.Comment;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.model.User;
import org.springframework.stereotype.Service;

@Service
public class CommentFactory {


    public Comment createComment(User user, Task task, CommentDTO commentDto) {

        return Comment.builder()
                .task(task)
                .text(commentDto.getText())
                .author(user)
                .build();
    }
}
