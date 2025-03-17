package org.example.userservice.factory;


import org.example.userservice.dto.CommentDTO;
import org.example.userservice.model.Comment;
import org.example.userservice.model.Task;
import org.example.userservice.model.User;
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
