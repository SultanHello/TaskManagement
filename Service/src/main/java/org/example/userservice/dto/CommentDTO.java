package org.example.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.model.Task;
import org.example.userservice.model.User;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private String text;

}
