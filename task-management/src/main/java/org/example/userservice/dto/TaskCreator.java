package org.example.userservice.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.model.Priority;
import org.example.userservice.model.Status;


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
