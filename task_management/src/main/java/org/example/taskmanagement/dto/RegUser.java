package org.example.taskmanagement.dto;


import lombok.Data;

import org.example.taskmanagement.model.Role;

@Data

public class RegUser {
    private String email;
    private String password;
    private Role role;
    public RegUser(String email,String password,Role role){
        this.email = email;
        this.password= password;
        this.role=role;

    }
}
