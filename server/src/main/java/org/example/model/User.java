package org.example.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;


//
//    public User(long generatedId, String firstName, String lastName, String patronymic, String login) {
//
//
//    }
}
