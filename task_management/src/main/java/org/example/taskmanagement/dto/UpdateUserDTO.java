package org.example.taskmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    @Email
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
