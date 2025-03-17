package org.example.userservice.dto;


import lombok.Data;
import org.example.userservice.model.Role;

@Data
public class LogUser {
    private String email;
    private String password;

    public LogUser(String email,String password){
        this.email = email;
        this.password= password;


    }
}
