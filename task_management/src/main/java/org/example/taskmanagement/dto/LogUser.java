package org.example.taskmanagement.dto;


import lombok.Data;

@Data
public class LogUser {
    private String email;
    private String password;

    public LogUser(String email,String password){
        this.email = email;
        this.password= password;


    }
}
