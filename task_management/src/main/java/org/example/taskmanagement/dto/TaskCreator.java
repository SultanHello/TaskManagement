package org.example.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taskmanagement.model.Priority;
import org.example.taskmanagement.model.Status;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreator {
    private String title;

    private String description;

    private Priority priority;

    private Status status;



    
}
