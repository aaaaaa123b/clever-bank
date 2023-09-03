package org.example.model;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;

}
